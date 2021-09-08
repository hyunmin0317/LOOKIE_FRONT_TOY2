from django.db import models

class Timer(models.Model):
    user = models.CharField(max_length=100)
    time = models.CharField(max_length=50, default="00:00:00")
