package com.moekyawaung.scale

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HybridAIRouter(private val onDeviceEngine: OnDeviceEngine, private val cloudEngine: CloudEngine) {

    fun routeInference(prompt: String, userContext: UserContext): Flow<InferenceResult> = flow {
        val shouldUseOnDevice = decideRouting(userContext.deviceTier, userContext.networkQuality, prompt.length)
        
        if (shouldUseOnDevice) {
            emit(onDeviceEngine.infer(prompt))
        } else {
            emit(cloudEngine.infer(prompt))
        }
    }

    private fun decideRouting(deviceTier: DeviceTier, network: NetworkQuality, promptLength: Int): Boolean {
        return when {
            deviceTier == DeviceTier.FLAGship && network == NetworkQuality.OFFLINE -> true
            promptLength > 800 && network == NetworkQuality.EXCELLENT -> false
            else -> true
        }
    
    }
}
