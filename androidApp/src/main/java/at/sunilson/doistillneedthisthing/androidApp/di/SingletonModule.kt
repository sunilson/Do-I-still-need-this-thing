package at.sunilson.doistillneedthisthing.androidApp.di

import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SingletonModule {
    @Provides
    fun provideImageLabeler(): ImageLabeler {
        return ImageLabeling.getClient(
            CustomImageLabelerOptions
                .Builder(LocalModel.Builder().setAssetFilePath("classifier.tflite").build())
                .setConfidenceThreshold(0.5f)
                .build()
        )
    }
}