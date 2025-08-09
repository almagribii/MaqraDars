// app/src/main/java/com/maqradars/MainActivity.kt

package com.maqradars

import MaqamListScreen
import MaqraDarsApp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.maqradars.data.MaqraDarsDatabase
import com.maqradars.data.entity.AyatExample
import com.maqradars.data.entity.GlosariumTerm
import com.maqradars.data.entity.Maqam
import com.maqradars.data.entity.MaqamVariant
import com.maqradars.data.repository.MaqamRepository
import com.maqradars.ui.screens.GlosariumScreen
import com.maqradars.ui.screens.MaqamDetailScreen
import com.maqradars.ui.screens.RecitationTypeSelectionScreen
import com.maqradars.ui.screens.SettingsScreen
import com.maqradars.ui.screens.TilawahScreen
import com.maqradars.ui.theme.MaqraDarsTheme
import com.maqradars.ui.viewmodel.MaqamViewModel
import com.maqradars.ui.viewmodel.MaqamViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.compose.foundation.isSystemInDarkTheme
import android.content.res.Configuration
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import android.media.MediaPlayer
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.maqradars.data.entity.User
import com.maqradars.ui.screens.PrivacyPolicyScreen
import com.maqradars.ui.screens.SplashScreen
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private val database by lazy { MaqraDarsDatabase.getDatabase(this) }
    private val repository by lazy {
        MaqamRepository(
            database.maqamDao(),
            database.maqamVariantDao(),
            database.ayatExampleDao(),
            database.glosariumTermDao(),
            database.userDao()
        )
    }
    private val viewModel: MaqamViewModel by viewModels { MaqamViewModelFactory(repository) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isSystemInDarkMode =
            (applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        addInitialData(isSystemInDarkMode)
        enableEdgeToEdge()
        setContent {
            var showSplashScreen by remember { mutableStateOf(true) }

            if (showSplashScreen) {
                SplashScreen(onTimeout = {
                    showSplashScreen = false
                })
            } else {
                val user by viewModel.user.collectAsState(initial = null)
                val isDarkMode = user?.isDarkMode ?: isSystemInDarkTheme()

                MaqraDarsTheme(darkTheme = isDarkMode) {
                    Surface(
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val navController = rememberNavController()
                        MaqraDarsApp(navController, viewModel)
                    }
                }
            }
        }
    }

    private fun addInitialData(isSystemInDarkMode: Boolean) {
        lifecycleScope.launch(Dispatchers.IO) {
            val maqamDao = database.maqamDao()
            val maqamVariantDao = database.maqamVariantDao()
            val ayatExampleDao = database.ayatExampleDao()
            val glosariumTermDao = database.glosariumTermDao()
            val userDao = database.userDao()

            if (maqamDao.getAllMaqamat().first().isEmpty()) {
                val bayatiId = maqamDao.insertMaqam(
                    Maqam(
                        name = "Bayati",
                        description = "Maqam yang paling sering digunakan untuk pembuka tilawah. Karakternya lembut, meliuk-liuk, dan terkesan khidmat.",
                        audioPathPureMaqam = "bayati.mp3",
                        isFavorite = false
                    )
                )
                val shobaId = maqamDao.insertMaqam(
                    Maqam(
                        name = "Shoba",
                        description = "Maqam ini memiliki karakter yang ringan, cepat, dan sendu. Iramanya halus dan lembut.",
                        audioPathPureMaqam = "shoba.mp3",
                        isFavorite = false
                    )
                )
                val hijazId = maqamDao.insertMaqam(
                    Maqam(
                        name = "Hijaz",
                        description = "Irama Hijaz memiliki ciri khas gerak lambat dan khas nuansa Timur Tengah. Iramanya tenang, indah, dan mendayu.",
                        audioPathPureMaqam = "hijaz.mp3",
                        isFavorite = false
                    )
                )
//                val karId = maqamDao.insertMaqam(
//                    Maqam(
//                        name = "Kar",
//                        description = "Maqam yang sering digunakan untuk transisi atau irama yang lebih dinamis.",
//                        audioPathPureMaqam = "kar.mp3",
//                        isFavorite = false
//                    )
//                )
                val rastId = maqamDao.insertMaqam(
                    Maqam(
                        name = "Rast",
                        description = "Dikenal dengan iramanya yang penuh semangat, lincah, dan tegas. Irama ini memiliki karakter yang ringan dan cepat.",
                        audioPathPureMaqam = "rast.mp3",
                        isFavorite = false
                    )
                )
                val shikaId = maqamDao.insertMaqam(
                    Maqam(
                        name = "Sika",
                        description = "Memiliki karakter yang lembut, khidmat, dan mendayu-dayu. Nada-nadanya cenderung bersifat riang dan cemerlang.",
                        audioPathPureMaqam = "sika.mp3",
                        isFavorite = false
                    )
                )
                val jiharkahId = maqamDao.insertMaqam(
                    Maqam(
                        name = "Jiharkah",
                        description = "Maqam ini memiliki karakter manis dan terkesan dalam. Iramanya dimulai dengan suara minor yang khas.",
                        audioPathPureMaqam = "jiharkah.mp3",
                        isFavorite = false
                    )
                )
                val nahawandId = maqamDao.insertMaqam(
                    Maqam(
                        name = "Nahawand",
                        description = "Memiliki nada yang indah, lembut, dan romantis. Karakter iramanya cenderung melankolis.",
                        audioPathPureMaqam = "nahawand.mp3",
                        isFavorite = false
                    )
                )

                val bayatiQararId = maqamVariantDao.insertMaqamVariant(
                    MaqamVariant(
                        maqamId = bayatiId,
                        variantName = "Qarar",
                        description = "Nada dasar yang stabil.",
                        audioPath = "bayati_qarar.mp3"
                    )
                )
                val bayatiNawaId = maqamVariantDao.insertMaqamVariant(
                    MaqamVariant(
                        maqamId = bayatiId,
                        variantName = "Nawa",
                        description = "Nada menengah yang stabil, sering menjadi jembatan antara nada awal dan nada tinggi.",
                        audioPath = "bayati_nawa.mp3"
                    )
                )
                val bayatiSuriId = maqamVariantDao.insertMaqamVariant(
                    MaqamVariant(
                        maqamId = bayatiId,
                        variantName = "Suri",
                        description = "Irama Bayati yang lebih cepat, energik, dan tegas.",
                        audioPath = "bayati_suri.mp3"
                    )
                )
                val bayatiHusainiId = maqamVariantDao.insertMaqamVariant(
                    MaqamVariant(
                        maqamId = bayatiId,
                        variantName = "Husaini",
                        description = "Nuansa yang lebih lembut dan tenang. Sering digunakan untuk doa.",
                        audioPath = "bayati_husaini.mp3"
                    )
                )
                val bayatiJawabId = maqamVariantDao.insertMaqamVariant(
                    MaqamVariant(
                        maqamId = bayatiId,
                        variantName = "Jawab",
                        description = "Nada jawaban, melantunkan dengan suara yang lebih tinggi.",
                        audioPath = "bayati_jawab.mp3"
                    )
                )
                val bayatiAsliJawabId = maqamVariantDao.insertMaqamVariant(
                    MaqamVariant(
                        maqamId = bayatiId,
                        variantName = "Asli Jawab",
                        description = "Nada jawaban asli yang lebih fokus dan stabil.",
                        audioPath = "bayati_aslijawab.mp3"
                    )
                )
                val bayatiSuriJawabJawabId = maqamVariantDao.insertMaqamVariant(
                    MaqamVariant(
                        maqamId = bayatiId,
                        variantName = "Suri Jawab Jawab",
                        description = "Varian Suri dengan nada jawaban yang berulang.",
                        audioPath = "bayati_surijawabjawab.mp3"
                    )
                )
                val bayatiAkhirQuflahId = maqamVariantDao.insertMaqamVariant(
                    MaqamVariant(
                        maqamId = bayatiId,
                        variantName = "Akhir Quflah",
                        description = "Varian penutup yang mengunci irama.",
                        audioPath = "bayati_akhirquflah.mp3"
                    )
                )

                val shobaAsliId = maqamVariantDao.insertMaqamVariant(
                    MaqamVariant(
                        maqamId = shobaId,
                        variantName = "Asli",
                        description = "Varian Shoba asli yang sendu dan halus.",
                        audioPath = "shoba_asli.mp3"
                    )
                )
                val shobaJawabId = maqamVariantDao.insertMaqamVariant(
                    MaqamVariant(
                        maqamId = shobaId,
                        variantName = "Jawab",
                        description = "Nada jawaban dari Shoba.",
                        audioPath = "shoba_jawab.mp3"
                    )
                )
                val shobaJawabBastanjariId = maqamVariantDao.insertMaqamVariant(
                    MaqamVariant(
                        maqamId = shobaId,
                        variantName = "Jawab ma'a Bastanjari",
                        description = "Varian Jawab yang dikombinasikan dengan Bastanjari.",
                        audioPath = "shoba_jawab_bastanjari.mp3"
                    )
                )

                val hijazAsliId = maqamVariantDao.insertMaqamVariant(
                    MaqamVariant(
                        maqamId = hijazId,
                        variantName = "Asli",
                        description = "Varian Hijaz asli yang klasik.",
                        audioPath = "hijaz_asli.mp3"
                    )
                )

//                val karAsliId = maqamVariantDao.insertMaqamVariant(
//                    MaqamVariant(
//                        maqamId = karId,
//                        variantName = "Asli",
//                        description = "Varian Kar asli.",
//                        audioPath = "kar_asli.mp3"
//                    )
//                )
//                val karKurdId = maqamVariantDao.insertMaqamVariant(
//                    MaqamVariant(
//                        maqamId = karId,
//                        variantName = "Kurd",
//                        description = "Perpaduan Kar dengan Maqam Kurd.",
//                        audioPath = "kar_kurd.mp3"
//                    )
//                )

                val rastAsliId = maqamVariantDao.insertMaqamVariant(
                    MaqamVariant(
                        maqamId = rastId,
                        variantName = "Asli",
                        description = "Varian Rast asli yang penuh semangat.",
                        audioPath = "rast_asli.mp3"
                    )
                )
                val rastJanjiranId = maqamVariantDao.insertMaqamVariant(
                    MaqamVariant(
                        maqamId = rastId,
                        variantName = "Janjiran",
                        description = "Varian Rast dengan irama Janjiran.",
                        audioPath = "rast_janjiran.mp3"
                    )
                )
                val rastAlaNawaId = maqamVariantDao.insertMaqamVariant(
                    MaqamVariant(
                        maqamId = rastId,
                        variantName = "Ala Nawa",
                        description = "Varian Rast dengan nada tinggi.",
                        audioPath = "rast_alanawa.mp3"
                    )
                )

                val shikaAsliId = maqamVariantDao.insertMaqamVariant(
                    MaqamVariant(
                        maqamId = shikaId,
                        variantName = "Asli",
                        description = "Varian Shika asli yang lembut dan riang.",
                        audioPath = "shika_asli.mp3"
                    )
                )
                val shikaMasriId = maqamVariantDao.insertMaqamVariant(
                    MaqamVariant(
                        maqamId = shikaId,
                        variantName = "Masri",
                        description = "Varian Shika Mesir.",
                        audioPath = "shika_masri.mp3"
                    )
                )
                val shikaTurkiId = maqamVariantDao.insertMaqamVariant(
                    MaqamVariant(
                        maqamId = shikaId,
                        variantName = "Turki",
                        description = "Varian Shika Turki.",
                        audioPath = "shika_turki.mp3"
                    )
                )

                val jiharkahAsliId = maqamVariantDao.insertMaqamVariant(
                    MaqamVariant(
                        maqamId = jiharkahId,
                        variantName = "Asli",
                        description = "Varian Jiharkah asli yang klasik.",
                        audioPath = "jiharkah_asli.mp3"
                    )
                )
                val jiharkahJawabId = maqamVariantDao.insertMaqamVariant(
                    MaqamVariant(
                        maqamId = jiharkahId,
                        variantName = "Jawab",
                        description = "Nada jawaban dari Jiharkah.",
                        audioPath = "jiharkah_jawab.mp3"
                    )
                )

                val nahawandAsliId = maqamVariantDao.insertMaqamVariant(
                    MaqamVariant(
                        maqamId = nahawandId,
                        variantName = "Asli",
                        description = "Varian Nahawand asli.",
                        audioPath = "nahawand_asli.mp3"
                    )
                )
                val nahawandJawabId = maqamVariantDao.insertMaqamVariant(
                    MaqamVariant(
                        maqamId = nahawandId,
                        variantName = "Jawab",
                        description = "Nada jawaban dari Nahawand.",
                        audioPath = "nahawand_jawab.mp3"
                    )
                )

                ayatExampleDao.insertAyatExample(
                    AyatExample(
                        maqamVariantId = bayatiNawaId,
                        surahNumber = 1,
                        ayatNumber = 1,
                        arabicText = "بِسْمِ ٱللّٰهِ ٱلرَّحْمٰنِ ٱلرَّحِيمِ",
                        translationText = "Dengan nama Allah Yang Maha Pengasih, Maha Penyayang.",
                        audioPath = "alfatihah_1"
                    )
                )
                ayatExampleDao.insertAyatExample(
                    AyatExample(
                        maqamVariantId = bayatiNawaId,
                        surahNumber = 1,
                        ayatNumber = 2,
                        arabicText = "اَلْحَمْدُ لِلّٰهِ رَبِّ الْعٰلَمِيْنَ",
                        translationText = "Segala puji bagi Allah, Tuhan seluruh alam,",
                        audioPath = "alfatihah_2"
                    )
                )
            }


            if (glosariumTermDao.getAllGlosariumTerms().first().isEmpty()) {
                val glosariumTerms = listOf(
                    GlosariumTerm(
                        term = "Maqam",
                        definition = "Tangga nada atau irama yang digunakan dalam pembacaan Al-Quran."
                    ),
                    GlosariumTerm(
                        term = "Maqam Bayati",
                        definition = "Maqam dasar yang paling umum, berkarakter lembut dan khidmat."
                    ),
                    GlosariumTerm(
                        term = "Maqam Hijaz",
                        definition = "Maqam dengan nuansa sedih, cocok untuk ayat-ayat yang membangkitkan perasaan duka."
                    ),
                    GlosariumTerm(
                        term = "Maqam Nahawand",
                        definition = "Maqam yang melodis dan romantis, sering digunakan pada ayat-ayat penuh cinta atau kesedihan."
                    ),
                    GlosariumTerm(
                        term = "Maqam Rast",
                        definition = "Maqam yang tegas dan penuh semangat, sering digunakan untuk azan."
                    ),
                    GlosariumTerm(
                        term = "Maqam Sika",
                        definition = "Maqam yang lembut dan riang, memberikan kesan keindahan dan keluhuran."
                    ),
                    GlosariumTerm(
                        term = "Maqam Shoba",
                        definition = "Maqam dengan irama sendu yang halus, cocok untuk ayat-ayat penyesalan."
                    ),
                    GlosariumTerm(
                        term = "Maqam Jiharkah",
                        definition = "Maqam yang ceria dan penuh semangat, sering dilantunkan saat takbiran."
                    ),
                    GlosariumTerm(
                        term = "Hukum Nun Mati",
                        definition = "Aturan bacaan yang berlaku saat huruf nun mati (نْ) atau tanwin bertemu huruf hijaiyah tertentu."
                    ),
                    GlosariumTerm(
                        term = "Izhar Halqi",
                        definition = "Membaca nun mati/tanwin dengan jelas tanpa dengung saat bertemu huruf tenggorokan (أ, ه, ع, ح, غ, خ)."
                    ),
                    GlosariumTerm(
                        term = "Idgham Bighunnah",
                        definition = "Meleburkan nun mati/tanwin ke huruf (ي, ن, م, و) dengan dengung, sepanjang dua harakat."
                    ),
                    GlosariumTerm(
                        term = "Idgham Bilaghunnah",
                        definition = "Meleburkan nun mati/tanwin ke huruf (ل, ر) tanpa dengung."
                    ),
                    GlosariumTerm(
                        term = "Iqlab",
                        definition = "Mengganti bunyi nun mati/tanwin menjadi mim (م) saat bertemu huruf ba (ب)."
                    ),
                    GlosariumTerm(
                        term = "Ikhfa' Haqiqi",
                        definition = "Menyamarkan nun mati/tanwin dengan dengung saat bertemu 15 huruf hijaiyah lainnya."
                    ),
                    GlosariumTerm(
                        term = "Hukum Mim Mati",
                        definition = "Aturan bacaan yang berlaku saat huruf mim mati (مْ) bertemu huruf hijaiyah tertentu."
                    ),
                    GlosariumTerm(
                        term = "Ikhfa' Syafawi",
                        definition = "Mim mati (مْ) bertemu ba (ب), dibaca samar dengan dengung, sepanjang dua harakat."
                    ),
                    GlosariumTerm(
                        term = "Idgham Mitslain",
                        definition = "Mim mati (مْ) bertemu mim (م), dibaca dengung dengan tasydid."
                    ),
                    GlosariumTerm(
                        term = "Izhar Syafawi",
                        definition = "Mim mati (مْ) bertemu huruf selain mim dan ba, dibaca jelas tanpa dengung."
                    ),
                    GlosariumTerm(
                        term = "Hukum Mad (Panjang Pendek)",
                        definition = "Aturan panjang-pendeknya bacaan dalam Al-Quran."
                    ),
                    GlosariumTerm(
                        term = "Mad Thabi'i",
                        definition = "Mad asli dengan panjang dua harakat."
                    ),
                    GlosariumTerm(
                        term = "Mad Far'i",
                        definition = "Mad cabang yang memiliki banyak jenis, dengan panjang lebih dari dua harakat."
                    ),
                    GlosariumTerm(
                        term = "Mad Wajib Muttasil",
                        definition = "Mad yang diikuti hamzah dalam satu kata, dibaca empat atau lima harakat."
                    ),
                    GlosariumTerm(
                        term = "Mad Jaiz Munfasil",
                        definition = "Mad yang diikuti hamzah di kata yang berbeda, dibaca dua, empat, atau lima harakat."
                    ),
                    GlosariumTerm(
                        term = "Mad Lazim",
                        definition = "Mad yang harus dibaca panjang enam harakat karena bertemu sukun asli."
                    ),
                    GlosariumTerm(
                        term = "Mad 'Arid Lissukun",
                        definition = "Mad thabi'i yang diikuti huruf sukun karena waqaf, dibaca dua, empat, atau enam harakat."
                    ),
                    GlosariumTerm(
                        term = "Mad Lin",
                        definition = "Mad yang terjadi ketika huruf wawu atau ya sukun didahului huruf berharakat fathah."
                    ),
                    GlosariumTerm(
                        term = "Mad Badal",
                        definition = "Mad yang terjadi dari hamzah bertemu alif, dibaca dua harakat."
                    ),
                    GlosariumTerm(
                        term = "Mad Silah Qasirah",
                        definition = "Mad pada ha dhomir yang tidak diikuti hamzah, dibaca dua harakat."
                    ),
                    GlosariumTerm(
                        term = "Mad Silah Tawilah",
                        definition = "Mad pada ha dhomir yang diikuti hamzah, dibaca empat atau lima harakat."
                    ),
                    GlosariumTerm(
                        term = "Hukum Ro'",
                        definition = "Aturan tebal-tipisnya bacaan huruf Ro' (ر)."
                    ),
                    GlosariumTerm(
                        term = "Ro' Tebal",
                        definition = "Ro' dibaca tebal (tafkhim) dalam kondisi tertentu."
                    ),
                    GlosariumTerm(
                        term = "Ro' Tipis",
                        definition = "Ro' dibaca tipis (tarqiq) dalam kondisi tertentu."
                    ),
                    GlosariumTerm(
                        term = "Lafadz Allah",
                        definition = "Cara membaca lafadz Allah (لله) dengan tebal atau tipis."
                    ),
                    GlosariumTerm(
                        term = "Sifatul Huruf",
                        definition = "Karakteristik atau sifat-sifat yang dimiliki oleh setiap huruf hijaiyah."
                    ),
                    GlosariumTerm(
                        term = "Hams",
                        definition = "Sifat huruf yang dibaca dengan suara berbisik dan mengeluarkan udara."
                    ),
                    GlosariumTerm(
                        term = "Jahr",
                        definition = "Sifat huruf yang dibaca dengan suara kuat dan tidak mengeluarkan udara."
                    ),
                    GlosariumTerm(
                        term = "Syiddah",
                        definition = "Sifat huruf yang ditahan suaranya saat diucapkan."
                    ),
                    GlosariumTerm(
                        term = "Rakhawah",
                        definition = "Sifat huruf yang suaranya mengalir saat diucapkan."
                    ),
                    GlosariumTerm(
                        term = "Isti'la",
                        definition = "Sifat huruf yang pangkal lidah naik ke langit-langit saat diucapkan (tebal)."
                    ),
                    GlosariumTerm(
                        term = "Istifal",
                        definition = "Sifat huruf yang pangkal lidah turun saat diucapkan (tipis)."
                    ),
                    GlosariumTerm(
                        term = "Makhrajul Huruf",
                        definition = "Tempat keluarnya suara atau huruf hijaiyah dari organ-organ bicara."
                    ),
                    GlosariumTerm(
                        term = "Al-Jauf",
                        definition = "Makhraj dari rongga mulut dan tenggorokan."
                    ),
                    GlosariumTerm(term = "Al-Halq", definition = "Makhraj dari tenggorokan."),
                    GlosariumTerm(term = "Al-Lisan", definition = "Makhraj dari lidah."),
                    GlosariumTerm(term = "As-Syafatan", definition = "Makhraj dari bibir."),
                    GlosariumTerm(
                        term = "Al-Khaisyum",
                        definition = "Makhraj dari pangkal hidung."
                    ),
                    GlosariumTerm(
                        term = "Waqaf",
                        definition = "Tanda berhenti dalam membaca Al-Quran."
                    ),
                    GlosariumTerm(
                        term = "Ibtida'",
                        definition = "Aturan memulai kembali bacaan setelah berhenti."
                    ),
                    GlosariumTerm(
                        term = "Waqaf Lazim",
                        definition = "Tanda berhenti yang harus dihentikan."
                    ),
                    GlosariumTerm(
                        term = "Waqaf Jaiz",
                        definition = "Tanda berhenti yang boleh dihentikan atau diteruskan."
                    ),
                    GlosariumTerm(
                        term = "Tafkhim",
                        definition = "Hukum bacaan yang melafalkan huruf dengan suara tebal."
                    ),
                    GlosariumTerm(
                        term = "Tarqiq",
                        definition = "Hukum bacaan yang melafalkan huruf dengan suara tipis."
                    ),
                    GlosariumTerm(
                        term = "Ikhfa' Syafawi",
                        definition = "Mim mati (مْ) bertemu ba (ب), dibaca samar dengan dengung, sepanjang dua harakat."
                    ),
                    GlosariumTerm(
                        term = "Idgham Mitslain",
                        definition = "Mim mati (مْ) bertemu mim (م), dibaca dengung dengan tasydid."
                    ),
                    GlosariumTerm(
                        term = "Izhar Syafawi",
                        definition = "Mim mati (مْ) bertemu huruf selain mim dan ba, dibaca jelas tanpa dengung."
                    ),
                    GlosariumTerm(
                        term = "Tarteel",
                        definition = "Membaca Al-Quran dengan perlahan, tenang, dan jelas, sesuai dengan aturan tajwid."
                    ),
                    GlosariumTerm(
                        term = "Murattal",
                        definition = "Gaya pembacaan yang lambat dan fokus pada kejelasan tajwid."
                    ),
                    GlosariumTerm(
                        term = "Mujawwad",
                        definition = "Gaya pembacaan yang menggunakan teknik vokal dan irama (maqam) yang indah."
                    ),
                    GlosariumTerm(
                        term = "Qari'",
                        definition = "Sebutan untuk orang yang membaca Al-Quran."
                    ),
                    GlosariumTerm(
                        term = "Rawi",
                        definition = "Orang yang meriwayatkan bacaan Al-Quran."
                    ),
                    GlosariumTerm(
                        term = "Isti'adzah",
                        definition = "Mengucapkan 'A'udzubillah...' sebelum memulai bacaan Al-Quran."
                    ),
                    GlosariumTerm(
                        term = "Basmalah",
                        definition = "Mengucapkan 'Bismillahirrahmanirrahim...' di awal setiap surah, kecuali Surah At-Taubah."
                    )
                )
                glosariumTermDao.insertAll(glosariumTerms)
            }
            if (userDao.getSingleUser().first() == null) {
                userDao.insertUser(User(username = "default_user", isDarkMode = isSystemInDarkMode))
            }
        }
    }

}

sealed class Screen(
    val route: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    data object MaqamList : Screen("maqam_list", "Maqamat", Icons.Default.Home)
    data object Glosarium : Screen("glosarium", "Glosarium", Icons.Default.Info)
    data object Settings : Screen("settings", "Pengaturan", Icons.Default.Settings)
    data object AskQori : Screen("ask", "Qori", Icons.Default.Message)
}




