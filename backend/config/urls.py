# mysite/urls.py
from django.urls import path, include
from django.contrib import admin

urlpatterns = [
    path("admin/", admin.site.urls),
    path("user/", include("api.urls")),
    path("api/auth", include("knox.urls")),
]