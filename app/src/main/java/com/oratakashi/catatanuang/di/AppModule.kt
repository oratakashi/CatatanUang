package com.oratakashi.catatanuang.di

import com.oratakashi.catatanuang.data.local.database.CatatanUangDatabase
import com.oratakashi.catatanuang.data.preferences.AppPreferences
import com.oratakashi.catatanuang.data.repository.TransaksiRepository
import com.oratakashi.catatanuang.domain.repository.ITransaksiRepository
import com.oratakashi.catatanuang.domain.usecase.AddTransaksiUseCase
import com.oratakashi.catatanuang.domain.usecase.DeleteTransaksiUseCase
import com.oratakashi.catatanuang.domain.usecase.GetHomeDataUseCase
import com.oratakashi.catatanuang.domain.usecase.GetLaporanBulananUseCase
import com.oratakashi.catatanuang.domain.usecase.GetLaporanHarianUseCase
import com.oratakashi.catatanuang.domain.usecase.GetLaporanMingguanUseCase
import com.oratakashi.catatanuang.domain.usecase.GetTransaksiByIdUseCase
import com.oratakashi.catatanuang.domain.usecase.UpdateTransaksiUseCase
import com.oratakashi.catatanuang.ui.detail.DetailViewModel
import com.oratakashi.catatanuang.ui.edit.EditViewModel
import com.oratakashi.catatanuang.ui.home.HomeViewModel
import com.oratakashi.catatanuang.ui.record.RecordViewModel
import com.oratakashi.catatanuang.ui.report.ReportViewModel
import com.oratakashi.catatanuang.ui.settings.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Main Koin DI module wiring all layers of the application.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 */
val AppModule = module {

    // Database
    single { CatatanUangDatabase.getInstance(androidContext()) }

    // DAO
    single { get<CatatanUangDatabase>().transaksiDao() }

    // Repository
    singleOf(::TransaksiRepository) bind ITransaksiRepository::class

    // Preferences
    single { AppPreferences(androidContext()) }

    // Use Cases
    factoryOf(::AddTransaksiUseCase)
    factoryOf(::UpdateTransaksiUseCase)
    factoryOf(::DeleteTransaksiUseCase)
    factoryOf(::GetTransaksiByIdUseCase)
    factoryOf(::GetHomeDataUseCase)
    factoryOf(::GetLaporanHarianUseCase)
    factoryOf(::GetLaporanMingguanUseCase)
    factoryOf(::GetLaporanBulananUseCase)

    // ViewModels
    viewModelOf(::HomeViewModel)
    viewModelOf(::RecordViewModel)
    viewModelOf(::ReportViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::DetailViewModel)
    viewModelOf(::EditViewModel)
}



