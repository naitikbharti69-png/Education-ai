package com.example.model

enum class AppLanguage(val label: String, val code: String) {
    HINDI_ENGLISH("Hinglish / द्विभाषी", "hinglish"),
    ENGLISH("English", "en"),
    HINDI("हिंदी", "hi")
}

fun AppLanguage.pick(en: String, hi: String, hinglish: String = "$en ($hi)"): String {
    return when (this) {
        AppLanguage.ENGLISH -> en
        AppLanguage.HINDI -> hi
        AppLanguage.HINDI_ENGLISH -> hinglish
    }
}

enum class SubjectStream(val labelEn: String, val labelHi: String) {
    ALL("All Streams", "सभी विषय"),
    FOUNDATION("Class 1–8 Foundation", "कक्षा 1–8 फाउंडेशन"),
    SCIENCE_MATH("Science & Maths (PCM/PCB)", "विज्ञान और गणित"),
    COMMERCE("Commerce & Economics", "वाणिज्य और अर्थशास्त्र"),
    HUMANITIES("Arts, History & Civics", "कला, इतिहास और नागरिक शास्त्र"),
    LANGUAGES("Hindi & English Grammar", "हिंदी और अंग्रेजी व्याकरण")
}

enum class DiagramType(val titleEn: String, val titleHi: String) {
    NUMBER_LINE("Number Line & Addition Jumps", "संख्या रेखा और जोड़"),
    FRACTION_PIE("Visual Fractions & Parts", "भिन्न चक्र (Fractions)"),
    WATER_CYCLE("Evaporation & Water Cycle", "जल चक्र (Water Cycle)"),
    PHOTOSYNTHESIS("Photosynthesis in Leaf", "प्रकाश संश्लेषण (Photosynthesis)"),
    SOLAR_SYSTEM("Solar System & Planetary Orbits", "सौर मंडल और ग्रहों की कक्षाएं"),
    CELL_STRUCTURE("Plant & Animal Cell Organelles", "कोशिका संरचना (Cell Structure)"),
    HUMAN_HEART("Human Heart Double Circulation", "मानव हृदय रक्त परिसंचरण"),
    TRIGONOMETRY_CIRCLE("Unit Circle & Trig Ratios", "त्रिकोणमिति इकाई वृत्त"),
    RAY_OPTICS("Convex Lens Ray Diagram", "उत्तल लेंस किरण आरेख"),
    ELECTRIC_CIRCUIT("Ohm's Law & Series-Parallel Circuit", "ओम का नियम और विद्युत परिपथ"),
    PARABOLA_GRAPH("Calculus Maxima, Minima & Tangents", "कलन (Calculus) स्पर्शरेखा और ग्राफ"),
    DEMAND_SUPPLY("Microeconomics Demand-Supply Equilibrium", "मांग और पूर्ति संतुलन वक्र")
}

data class LectureChapter(
    val timestampLabel: String,
    val titleEn: String,
    val titleHi: String,
    val progressFraction: Float
)

data class LectureItem(
    val id: String,
    val classNumber: Int, // 1 to 12
    val subjectEn: String,
    val subjectHi: String,
    val stream: SubjectStream,
    val titleEn: String,
    val titleHi: String,
    val instructor: String,
    val durationMinutes: Int,
    val xpReward: Int,
    val diagramType: DiagramType,
    val chapters: List<LectureChapter>,
    val keyFormulas: List<String>,
    val transcriptEn: String,
    val transcriptHi: String,
    val defaultSummaryEn: List<String>,
    val defaultSummaryHi: List<String>,
    val diagramLabelsEn: List<String>,
    val diagramLabelsHi: List<String>
)

data class MockQuestion(
    val id: String,
    val questionEn: String,
    val questionHi: String,
    val optionsEn: List<String>,
    val optionsHi: List<String>,
    val correctIndex: Int,
    val explanationEn: String,
    val explanationHi: String
)

data class MockTestItem(
    val id: String,
    val classNumber: Int,
    val subjectEn: String,
    val subjectHi: String,
    val stream: SubjectStream,
    val titleEn: String,
    val titleHi: String,
    val durationMinutes: Int,
    val xpReward: Int,
    val questions: List<MockQuestion>
)

data class AiGeneratedSummary(
    val lectureId: String,
    val summaryBullets: List<String>,
    val keyFormulasOrFacts: List<String>,
    val diagramDrawingSteps: List<String>,
    val examTip: String
)

data class DrawnStrokePoint(
    val xRatio: Float,
    val yRatio: Float
)

data class DrawnStroke(
    val points: List<DrawnStrokePoint>,
    val colorHex: Long,
    val strokeWidth: Float,
    val shapeType: String = "FREEHAND", // FREEHAND, CIRCLE, RECT, ARROW, TEXT_LABEL
    val labelText: String = ""
)

data class ChatMessage(
    val id: String,
    val isUser: Boolean,
    val text: String,
    val subjectTag: String = "",
    val isStreaming: Boolean = false,
    val isError: Boolean = false
)

data class GamifiedBadge(
    val id: String,
    val titleEn: String,
    val titleHi: String,
    val descEn: String,
    val descHi: String,
    val requiredXp: Int,
    val unlocked: Boolean
)
