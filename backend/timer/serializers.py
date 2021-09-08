# api/serializers.py
from rest_framework import serializers
from .models import Timer
from django.contrib.auth import authenticate

class PostSerializer(serializers.ModelSerializer):
    class Meta:
        model = Timer
        fields = '__all__'

class CreateSerializer(serializers.ModelSerializer):
    user = serializers.ReadOnlyField(source='user.id')
    class Meta:
        model = Timer
        fields = '__all__'