package com.survey4all

import com.survey4all.Route.*
import com.survey4all.models.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*

enum class Route(val path: String, val auth: Boolean = true) {
    SignIn("/signin", false),
    SignUp("/signup", false),
    Profile("/profile"),
    Feed("/feed"),
    Surveys("/surveys"),
    SurveyById("/survey/{id}"),
    Voting("/vote")
}

fun Application.configureRouting(repo: Repo) = routing {

    post(SignUp.path) {
        val request = call.receive<SignUpRequest>()
        val user = repo.addUser(request.data, request.password)
        val surveys = repo.getUserSurveys(user)
        call.respond(AuthResponse(user.token, ProfileResponse(user.data, UserVotesData(), surveys)))
    }

    post(SignIn.path) {
        val request = call.receive<AuthRequest>()
        val token = repo.updateToken(request.email, request.password)
        val user = requireNotNull(repo.getUser(token)) { "Auth internal error" }
        val surveys = repo.getUserSurveys(user)
        val votes = repo.getUserVotes(user)
        call.respond(AuthResponse(token, ProfileResponse(user.data, votes, surveys)))
    }

    get(Profile.path) {
        val surveys = repo.getUserSurveys(currentUser)
        val votes = repo.getUserVotes(currentUser)
        call.respond(ProfileResponse(currentUser.data, votes, surveys))
    }

    put(Profile.path) {
        val request = call.receive<EditProfileRequest>()
        val updatedUser = repo.updateUser(currentUser, request.name, request.age, request.sex, request.countryCode)
        call.respond(updatedUser.data)
    }

    get(Feed.path) {
        val startAfter = call.request.queryParameters["startAfter"]
        val count = call.request.queryParameters["count"]?.toIntOrNull()
        val surveys = repo.getFeed(currentUser, count ?: 50, startAfter)
        if (surveys == null) {
            call.respond(HttpStatusCode.NotFound, "Survey not found")
        } else {
            call.respond(surveys)
        }
    }

    post(Surveys.path) {
        val request = call.receive<CreateSurveyRequest>()
        val survey = repo.addSurvey(currentUser, request)
        call.respond(survey)
    }

    get(SurveyById.path) {
        val id = call.parameters.getOrFail("id")
        val survey = repo.getSurvey(id)
        if (survey == null) {
            call.respond(HttpStatusCode.NotFound, "Survey not found")
        } else {
            call.respond(survey)
        }
    }

    put(SurveyById.path) {
        val request = call.receive<EditSurveyRequest>()
        val id = call.parameters.getOrFail("id")
        val updatedSurvey = repo.updateSurvey(currentUser, id, request.title, request.desc)
        requireNotNull(updatedSurvey) { "Survey not found" }
        call.respond(updatedSurvey)
    }

    post(Voting.path) {
        val request = call.receive<VoteRequest>()
        val survey = repo.vote(currentUser, request.surveyId, request.vote)
        if (survey == null) {
            call.respond(HttpStatusCode.NotFound, "Survey not found")
        } else {
            call.respond(survey)
        }
    }

    install(StatusPages) {
        exception<Exception> { cause ->
            call.respond(HttpStatusCode.InternalServerError, cause.message.toString())
        }
    }
}
