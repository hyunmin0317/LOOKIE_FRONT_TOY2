# api/urls.py
from django.urls import path, include
from .views import HelloAPI, RegistrationAPI, LoginAPI, UserAPI

urlpatterns = [
    path("hello/", HelloAPI),
    path("signup/", RegistrationAPI.as_view()),
    path("login/", LoginAPI.as_view()),
    path("user/", UserAPI.as_view()),
]