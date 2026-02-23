package com.entourageapp.features.projects.domain

interface ProjectsRepository {
    fun getProjectsList(): List<Project>
}