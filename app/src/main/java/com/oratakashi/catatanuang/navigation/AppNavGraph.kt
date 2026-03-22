package com.oratakashi.catatanuang.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.oratakashi.catatanuang.ui.detail.DetailScreen
import com.oratakashi.catatanuang.ui.edit.EditScreen
import com.oratakashi.catatanuang.ui.home.HomeScreen
import com.oratakashi.catatanuang.ui.record.RecordScreen
import com.oratakashi.catatanuang.ui.report.ReportScreen
import com.oratakashi.catatanuang.ui.settings.SettingsScreen

/**
 * Root navigation graph for the application.
 * Wires all screens to their respective routes.
 *
 * Detail screens (RecordFinance, Report, Settings) slide in from the right on navigate and
 * slide out to the right on back — providing a natural push/pop feel.
 *
 * @author oratakashi
 * @since 22 Mar 2026
 */
@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToRecord = { navController.navigate(Screen.RecordFinance.route) },
                onNavigateToReport = { navController.navigate(Screen.Report.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onNavigateToDetail = { id ->
                    navController.navigate(Screen.TransaksiDetail.createRoute(id))
                }
            )
        }
        composable(
            route = Screen.RecordFinance.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { it }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
        ) {
            RecordScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.Report.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { it }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
        ) {
            ReportScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { id ->
                    navController.navigate(Screen.TransaksiDetail.createRoute(id))
                }
            )
        }
        composable(
            route = Screen.Settings.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { it }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
        ) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.TransaksiDetail.route,
            arguments = listOf(navArgument("transaksiId") { type = NavType.IntType }),
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { it }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
        ) { backStackEntry ->
            val transaksiId = backStackEntry.arguments?.getInt("transaksiId") ?: return@composable
            DetailScreen(
                transaksiId = transaksiId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { id ->
                    navController.navigate(Screen.EditTransaksi.createRoute(id))
                }
            )
        }
        composable(
            route = Screen.EditTransaksi.route,
            arguments = listOf(navArgument("transaksiId") { type = NavType.IntType }),
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { it }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
        ) { backStackEntry ->
            val transaksiId = backStackEntry.arguments?.getInt("transaksiId") ?: return@composable
            EditScreen(
                transaksiId = transaksiId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
