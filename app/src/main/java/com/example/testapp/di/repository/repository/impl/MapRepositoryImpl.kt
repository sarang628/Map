package com.sryang.torang_repository.di.repository.repository.impl

import com.sryang.torang_repository.preference.TorangPreference
import com.sryang.torang_repository.repository.MapRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapRepositoryImpl @Inject constructor(
    private val torangPreference: TorangPreference
) : MapRepository {
    override fun saveLat(lat: Double) {
        torangPreference.saveLat(lat)
    }

    override fun savelon(lon: Double) {
        torangPreference.saveLon(lon)
    }

    override fun saveZoom(level: Float) {
        torangPreference.saveZoom(level)
    }

    override fun loadLat(): Double {
        return torangPreference.loadLat()
    }

    override fun loadLon(): Double {
        return torangPreference.loadLon()
    }

    override fun loadZoom(): Float {
        return torangPreference.loadZoom()
    }
}