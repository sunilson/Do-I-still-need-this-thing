package at.sunilson.doistillneedthisthing.androidApp.di

import android.content.Context
import android.content.SharedPreferences
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("doIStillNeedThisThing", Context.MODE_PRIVATE)
    }
}