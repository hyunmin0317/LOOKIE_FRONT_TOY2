from django.shortcuts import render

from rest_framework.generics import ListAPIView, CreateAPIView, GenericAPIView
from rest_framework.response import Response
from rest_framework.decorators import api_view
from .serializers import PostSerializer, CreateSerializer
from .models import Timer

class UpdateAPI(CreateAPIView):
    serializer_class = CreateSerializer
    def perform_create(self, serializer):
        serializer.save(user=self.request.user.username)

class TimerAPI(ListAPIView):
    queryset = Timer.objects.all()
    serializer_class = PostSerializer

class MyTimerAPI(ListAPIView):
    serializer_class = PostSerializer
    def get_queryset(self):
        return Timer.objects.filter(user=self.request.user.username)