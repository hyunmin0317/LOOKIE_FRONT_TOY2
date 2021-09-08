# api/urls.py
from django.urls import path
from .views import UpdateAPI, TimerAPI, MyTimerAPI

urlpatterns = [
    path("", UpdateAPI.as_view()),
    path("all", TimerAPI.as_view()),
    path("mytimer", MyTimerAPI.as_view())
]