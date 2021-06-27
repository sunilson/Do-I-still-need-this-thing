package at.sunilson.doistillneedthisthing.shared.di

import at.sunilson.doistillneedthisthing.Database
import at.sunilson.doistillneedthisthing.shared.data.createDatabase
import at.sunilson.doistillneedthisthing.shared.domain.AddItem
import at.sunilson.doistillneedthisthing.shared.domain.GetItemsDeleted
import at.sunilson.doistillneedthisthing.shared.domain.GetItemsNotNeeded
import at.sunilson.doistillneedthisthing.shared.domain.GetItemsStillNeeded
import at.sunilson.doistillneedthisthing.shared.domain.GetRandomDecision
import at.sunilson.doistillneedthisthing.shared.domain.GetSettings
import at.sunilson.doistillneedthisthing.shared.domain.MarkItemAsChecked
import at.sunilson.doistillneedthisthing.shared.domain.MarkItemAsNeeded
import at.sunilson.doistillneedthisthing.shared.domain.MarkItemAsNotNeeded
import at.sunilson.doistillneedthisthing.shared.domain.MarkItemAsRemoved
import at.sunilson.doistillneedthisthing.shared.domain.UpdateSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedModule {

    @Provides
    @Singleton
    fun provideDatabase(): Database {
        return createDatabase()
    }

    @Provides
    fun provideGetItemsStillNeeded(database: Database) = GetItemsStillNeeded(database)

    @Provides
    fun provideGetItemsNotNeeded(database: Database) = GetItemsNotNeeded(database)

    @Provides
    fun provideGetItemsDeleted(database: Database) = GetItemsDeleted(database)

    @Provides
    fun provideAddItem(database: Database) = AddItem(database)

    @Provides
    fun provideMarkItemAsNotNeeded(database: Database) = MarkItemAsNotNeeded(database)

    @Provides
    fun provideMarkItemAsNeeded(database: Database) = MarkItemAsNeeded(database)

    @Provides
    fun provideMarkItemAsRemoved(database: Database) = MarkItemAsRemoved(database)

    @Provides
    fun provideMarkItemAsChecked(database: Database) = MarkItemAsChecked(database)

    @Provides
    fun provideGetSettings(database: Database) = GetSettings(database)

    @Provides
    fun provideUpdateSettings(database: Database) = UpdateSettings(database)

    @Provides
    fun provideGetRandomDecisions(
        getSettings: GetSettings,
        database: Database
    ) = GetRandomDecision(getSettings, database)
}