package com.example.data

import com.example.model.DiagramType
import com.example.model.LectureChapter
import com.example.model.LectureItem
import com.example.model.MockQuestion
import com.example.model.MockTestItem
import com.example.model.SubjectStream

object CurriculumCatalog {

    val lectures: List<LectureItem> = listOf(
        // Class 1
        LectureItem(
            id = "lec_c1_math",
            classNumber = 1,
            subjectEn = "Mathematics",
            subjectHi = "गणित (Maths)",
            stream = SubjectStream.FOUNDATION,
            titleEn = "Numbers 1 to 20 & Jumping on Number Line",
            titleHi = "1 से 20 तक संख्याएँ और संख्या रेखा पर कूद",
            instructor = "Anjali Verma Ma'am",
            durationMinutes = 14,
            xpReward = 40,
            diagramType = DiagramType.NUMBER_LINE,
            chapters = listOf(
                LectureChapter("00:00", "Counting Objects 1-10", "1 से 10 तक वस्तुओं की गिनती", 0.0f),
                LectureChapter("04:30", "Forward Jumps for Addition", "जोड़ के लिए आगे कूदना", 0.35f),
                LectureChapter("09:15", "Backward Jumps for Subtraction", "घटाव के लिए पीछे कूदना", 0.70f)
            ),
            keyFormulas = listOf("3 + 2 = 5 (Start at 3, jump 2 steps right)", "7 - 3 = 4 (Start at 7, jump 3 steps left)"),
            transcriptEn = "Hello little champions! Today we will learn counting from 1 to 20 and how a frog jumps forward on a number line to add numbers and backward to subtract.",
            transcriptHi = "नमस्ते प्यारे बच्चों! आज हम 1 से 20 तक गिनती सीखेंगे और देखेंगे कि संख्या रेखा पर मेंढक की तरह आगे कूदकर जोड़ (Addition) और पीछे कूदकर घटाव (Subtraction) कैसे करते हैं।",
            defaultSummaryEn = listOf(
                "Numbers increase as we move from left to right on the number line.",
                "Addition (+) means jumping forward to the right.",
                "Subtraction (-) means jumping backward to the left.",
                "Zero (0) means absence of any object."
            ),
            defaultSummaryHi = listOf(
                "संख्या रेखा पर बाएं से दाएं जाने पर संख्याएं बढ़ती हैं।",
                "जोड़ (+) का अर्थ है दाईं ओर आगे कूदना।",
                "घटाव (-) का अर्थ है बाईं ओर पीछे कूदना।",
                "शून्य (0) का अर्थ है कोई भी वस्तु न होना।"
            ),
            diagramLabelsEn = listOf("Origin (0)", "Forward Jump (+2)", "Landing Point (5)", "Number Scale (0..10)"),
            diagramLabelsHi = listOf("शुरुआती बिंदु (0)", "आगे की छलांग (+2)", "पहुँच बिंदु (5)", "संख्या पैमाना (0..10)")
        ),
        // Class 2
        LectureItem(
            id = "lec_c2_lang",
            classNumber = 2,
            subjectEn = "Hindi & English Grammar",
            subjectHi = "हिंदी एवं अंग्रेजी व्याकरण",
            stream = SubjectStream.LANGUAGES,
            titleEn = "Nouns (Sangya) & Action Words with Visual Cards",
            titleHi = "संज्ञा (Nouns) और क्रिया शब्द चित्रों के साथ",
            instructor = "Kiran Sharma Ma'am",
            durationMinutes = 16,
            xpReward = 45,
            diagramType = DiagramType.FRACTION_PIE,
            chapters = listOf(
                LectureChapter("00:00", "Naming Words (Person, Place, Thing)", "नाम वाले शब्द (व्यक्ति, स्थान, वस्तु)", 0.0f),
                LectureChapter("06:00", "Action Words (Verbs / Kriya)", "काम वाले शब्द (क्रिया)", 0.45f),
                LectureChapter("11:30", "Sentence Building Practice", "वाक्य निर्माण अभ्यास", 0.78f)
            ),
            keyFormulas = listOf("Noun (संज्ञा) = Person + Place + Animal + Thing", "Verb (क्रिया) = Action / Doing Word (Read, Play, दौड़ना)"),
            transcriptEn = "Every object around us has a name! Words like Riya, Delhi, Tiger, and Book are Nouns (Sangya). Words that tell what someone is doing like running or reading are Verbs (Kriya).",
            transcriptHi = "हमारे चारों ओर हर चीज़ का एक नाम होता है! किसी व्यक्ति, वस्तु, स्थान या भाव के नाम को संज्ञा (Noun) कहते हैं, और काम करने वाले शब्दों को क्रिया (Verb) कहते हैं।",
            defaultSummaryEn = listOf(
                "Nouns (संज्ञा) name people, places, animals, and things.",
                "Proper nouns always start with a capital letter in English.",
                "Verbs (क्रिया) show actions such as eating, jumping, or writing."
            ),
            defaultSummaryHi = listOf(
                "संज्ञा (Noun) किसी व्यक्ति, स्थान, जानवर या वस्तु के नाम को कहते हैं।",
                "अंग्रेजी में व्यक्तिवाचक संज्ञा (Proper Noun) बड़े अक्षर से शुरू होती है।",
                "क्रिया (Verb) से किसी काम के करने या होने का पता चलता है जैसे पढ़ना, खेलना।"
            ),
            diagramLabelsEn = listOf("Person (व्यक्ति)", "Place (स्थान)", "Animal (प्राणी)", "Thing (वस्तु)"),
            diagramLabelsHi = listOf("व्यक्ति (राम, रिया)", "स्थान (स्कूल, दिल्ली)", "प्राणी (शेर, मोर)", "वस्तु (किताब, कलम)")
        ),
        // Class 3
        LectureItem(
            id = "lec_c3_evs",
            classNumber = 3,
            subjectEn = "Environmental Science (EVS)",
            subjectHi = "पर्यावरण अध्ययन (EVS)",
            stream = SubjectStream.FOUNDATION,
            titleEn = "Water Cycle: Evaporation, Clouds & Rain",
            titleHi = "जल चक्र: वाष्पीकरण, बादल और वर्षा",
            instructor = "Rohit Nair Sir",
            durationMinutes = 18,
            xpReward = 50,
            diagramType = DiagramType.WATER_CYCLE,
            chapters = listOf(
                LectureChapter("00:00", "Sun Heats Rivers & Oceans", "सूर्य की गर्मी से जल का गर्म होना", 0.0f),
                LectureChapter("06:20", "Evaporation & Condensation", "वाष्पीकरण और संघनन (बादल बनना)", 0.40f),
                LectureChapter("12:40", "Precipitation (Rain) & Collection", "वर्षा और जल संचयन", 0.75f)
            ),
            keyFormulas = listOf("Evaporation: Liquid Water -> Water Vapour", "Condensation: Water Vapour -> Tiny Cloud Droplets", "Precipitation: Heavy Clouds -> Rain/Snow"),
            transcriptEn = "When the bright sun shines on oceans and rivers, water turns into invisible vapour and rises up. This is Evaporation! High in the cool sky, vapour forms clouds (Condensation) and falls back as rain (Precipitation).",
            transcriptHi = "जब सूरज की धूप नदी और समुद्र के पानी पर पड़ती है, तो पानी भाप बनकर ऊपर उठता है जिसे वाष्पीकरण (Evaporation) कहते हैं। ऊपर ठंडी हवा में भाप से बादल बनते हैं (संघनन) और फिर बारिश होती है।",
            defaultSummaryEn = listOf(
                "Evaporation: Sun heats water in rivers/oceans into rising water vapour.",
                "Condensation: Rising vapour cools high in the sky to form clouds.",
                "Precipitation: Water droplets become heavy and fall as rain or snow.",
                "Collection: Rainwater flows back into rivers, lakes, and groundwater."
            ),
            defaultSummaryHi = listOf(
                "वाष्पीकरण (Evaporation): सूर्य की गर्मी से पानी का भाप बनकर ऊपर उठना।",
                "संघनन (Condensation): भाप का ठंडा होकर बादलों में बदलना।",
                "वर्षण (Precipitation): बादलों से बारिश या बर्फ के रूप में पानी गिरना।",
                "संग्रहण: बारिश का पानी वापस नदियों और भूजल में मिल जाना।"
            ),
            diagramLabelsEn = listOf("Sun (Heat Source)", "Evaporation Arrows", "Condensation Cloud", "Rain Precipitation"),
            diagramLabelsHi = listOf("सूर्य (ऊष्मा स्रोत)", "वाष्पीकरण (भाप)", "संघनन (बादल)", "वर्षा (बारिश)")
        ),
        // Class 4
        LectureItem(
            id = "lec_c4_math",
            classNumber = 4,
            subjectEn = "Mathematics",
            subjectHi = "गणित (Maths)",
            stream = SubjectStream.FOUNDATION,
            titleEn = "Fractions: Halves, Quarters & Equivalent Parts",
            titleHi = "भिन्न (Fractions): आधा, चौथाई और तुल्य भिन्न",
            instructor = "Pradeep Kulkarni Sir",
            durationMinutes = 20,
            xpReward = 55,
            diagramType = DiagramType.FRACTION_PIE,
            chapters = listOf(
                LectureChapter("00:00", "Numerator & Denominator Meaning", "अंश (Numerator) और हर (Denominator)", 0.0f),
                LectureChapter("07:10", "Visualizing 1/2, 1/4, and 3/4", "1/2, 1/4 और 3/4 को चित्र से समझना", 0.42f),
                LectureChapter("14:00", "Equivalent Fractions (1/2 = 2/4)", "तुल्य भिन्न (1/2 = 2/4 = 4/8)", 0.76f)
            ),
            keyFormulas = listOf("Fraction = Numerator (अंश) / Denominator (हर)", "1/2 = 2/4 = 3/6 = 4/8", "1/4 + 3/4 = 4/4 = 1 Whole"),
            transcriptEn = "Imagine sharing a delicious pizza equally among 4 friends. Each slice is 1 out of 4 equal parts, written as 1/4! The top number is the Numerator (Ansh) and the bottom number is the Denominator (Har).",
            transcriptHi = "कल्पना कीजिए कि आप एक रोटी या पिज़्ज़ा को 4 बराबर भागों में बाँटते हैं। प्रत्येक भाग को 1/4 (एक-चौथाई) कहते हैं। ऊपर की संख्या को अंश (Numerator) और नीचे की कुल संख्या को हर (Denominator) कहते हैं।",
            defaultSummaryEn = listOf(
                "A fraction represents equal parts of a whole object or collection.",
                "Numerator (top) shows selected parts; Denominator (bottom) shows total equal parts.",
                "Equivalent fractions have different numbers but equal value (e.g., 1/2 = 2/4)."
            ),
            defaultSummaryHi = listOf(
                "भिन्न (Fraction) किसी पूर्ण वस्तु के बराबर हिस्सों को दर्शाती है।",
                "अंश (ऊपर) चुने गए भाग दिखाता है; हर (नीचे) कुल बराबर भाग दिखाता है।",
                "तुल्य भिन्न (Equivalent Fractions) दिखने में अलग होती हैं पर उनका मान बराबर होता है (जैसे 1/2 = 2/4)।"
            ),
            diagramLabelsEn = listOf("Whole Circle (4/4)", "Shaded Quarter (1/4)", "Numerator (Top)", "Denominator (Bottom)"),
            diagramLabelsHi = listOf("पूर्ण वृत्त (4/4)", "छायांकित भाग (1/4)", "अंश (Numerator)", "हर (Denominator)")
        ),
        // Class 5
        LectureItem(
            id = "lec_c5_sci",
            classNumber = 5,
            subjectEn = "Science",
            subjectHi = "विज्ञान (Science)",
            stream = SubjectStream.FOUNDATION,
            titleEn = "How Green Plants Make Food: Photosynthesis",
            titleHi = "पौधे अपना भोजन कैसे बनाते हैं: प्रकाश संश्लेषण",
            instructor = "Dr. Meenakshi Rao",
            durationMinutes = 22,
            xpReward = 60,
            diagramType = DiagramType.PHOTOSYNTHESIS,
            chapters = listOf(
                LectureChapter("00:00", "Chlorophyll & Sunlight Energy", "क्लोरोफिल (पर्णहरित) और सूर्य का प्रकाश", 0.0f),
                LectureChapter("08:00", "Stomata, CO2 & Root Water Absorption", "रंध्र (Stomata), कार्बन डाइऑक्साइड और जल", 0.40f),
                LectureChapter("15:30", "Glucose Production & Oxygen Release", "ग्लूकोज निर्माण और ऑक्सीजन का उत्सर्जन", 0.75f)
            ),
            keyFormulas = listOf("6CO2 + 6H2O + Sunlight (Chlorophyll) -> C6H12O6 (Glucose) + 6O2"),
            transcriptEn = "Green leaves are the kitchen of a plant! Using chlorophyll, leaves trap sunlight, take carbon dioxide from air through tiny pores called stomata, and absorb water from roots to make glucose and release life-giving oxygen.",
            transcriptHi = "हरी पत्तियाँ पौधे का रसोईघर होती हैं! पत्तियों में मौजूद क्लोरोफिल सूर्य के प्रकाश को सोखता है, हवा से कार्बन डाइऑक्साइड (CO2) और जड़ों से पानी लेकर भोजन (ग्लूकोज) बनाता है और ऑक्सीजन गैस छोड़ता है।",
            defaultSummaryEn = listOf(
                "Photosynthesis occurs in green leaves containing Chlorophyll.",
                "Raw materials needed: Sunlight, Water (from roots), and Carbon Dioxide (through stomata).",
                "Products formed: Glucose (plant food/starch) and Oxygen gas released into air."
            ),
            defaultSummaryHi = listOf(
                "प्रकाश संश्लेषण हरी पत्तियों में क्लोरोफिल की उपस्थिति में होता है।",
                "आवश्यक सामग्री: सूर्य का प्रकाश, जल (जड़ों से), और कार्बन डाइऑक्साइड (रंध्रों से)।",
                "उत्पाद: ग्लूकोज (पौधे का भोजन) और प्राणवायु ऑक्सीजन (O2)।"
            ),
            diagramLabelsEn = listOf("Sunlight Energy", "CO2 Intake (Stomata)", "Water from Roots (Xylem)", "O2 Released + Glucose"),
            diagramLabelsHi = listOf("सूर्य का प्रकाश", "CO2 प्रवेश (रंध्र)", "जड़ों से जल (जाइलम)", "ऑक्सीजन + ग्लूकोज")
        ),
        // Class 6
        LectureItem(
            id = "lec_c6_sst",
            classNumber = 6,
            subjectEn = "Social Science (Geography)",
            subjectHi = "सामाजिक विज्ञान (भूगोल)",
            stream = SubjectStream.FOUNDATION,
            titleEn = "The Earth in the Solar System & Planetary Orbits",
            titleHi = "सौरमंडल में पृथ्वी और ग्रहों की कक्षाएँ",
            instructor = "Vikramaditya Singh Sir",
            durationMinutes = 24,
            xpReward = 65,
            diagramType = DiagramType.SOLAR_SYSTEM,
            chapters = listOf(
                LectureChapter("00:00", "The Sun & 8 Planets in Order", "सूर्य और क्रम से 8 ग्रह", 0.0f),
                LectureChapter("09:00", "Inner Rocky vs Outer Gas Giants", "आंतरिक चट्टानी ग्रह बनाम बाहरी गैसीय ग्रह", 0.42f),
                LectureChapter("17:15", "Why Earth is the Blue Planet (Goldilocks Zone)", "पृथ्वी को नीला ग्रह क्यों कहते हैं?", 0.78f)
            ),
            keyFormulas = listOf("Order from Sun: Mercury, Venus, Earth, Mars, Jupiter, Saturn, Uranus, Neptune", "Earth Revolution = 365¼ Days | Rotation = 24 Hours"),
            transcriptEn = "Our Solar System has the Sun at the center and 8 planets revolving in elliptical paths called orbits. Mercury is closest, Venus is hottest, Earth is our Blue Planet with life, and Mars is the Red Planet!",
            transcriptHi = "हमारे सौरमंडल के केंद्र में सूर्य है और 8 ग्रह दीर्घवृत्ताकार पथ (कक्षा/Orbit) में उसकी परिक्रमा करते हैं। बुध सबसे पास है, शुक्र सबसे गर्म है, पृथ्वी जीवन वाला नीला ग्रह है और मंगल लाल ग्रह है!",
            defaultSummaryEn = listOf(
                "Mnemonic for 8 planets: My Very Educated Mother Just Served Us Noodles.",
                "Inner planets (Mercury, Venus, Earth, Mars) are rocky; Outer planets are gas/ice giants.",
                "Asteroid belt lies between the orbits of Mars and Jupiter."
            ),
            defaultSummaryHi = listOf(
                "सूर्य से दूरी के अनुसार 8 ग्रह: बुध, शुक्र, पृथ्वी, मंगल, बृहस्पति, शनि, अरुण, वरुण।",
                "पृथ्वी का दो-तिहाई भाग जल से ढका होने के कारण इसे 'नीला ग्रह' (Blue Planet) कहते हैं।",
                "मंगल और बृहस्पति की कक्षाओं के बीच क्षुद्रग्रह पट्टी (Asteroid Belt) पाई जाती है।"
            ),
            diagramLabelsEn = listOf("Sun (Center Star)", "Inner Orbits (Mercury-Mars)", "Asteroid Belt", "Outer Giants (Jupiter-Neptune)"),
            diagramLabelsHi = listOf("सूर्य (केंद्र)", "आंतरिक ग्रह (बुध-मंगल)", "क्षुद्रग्रह पट्टी", "बाहरी ग्रह (बृहस्पति-वरुण)")
        ),
        // Class 7
        LectureItem(
            id = "lec_c7_sci",
            classNumber = 7,
            subjectEn = "Science (Physics)",
            subjectHi = "विज्ञान (भौतिकी)",
            stream = SubjectStream.FOUNDATION,
            titleEn = "Electric Current, Symbols & Simple Circuits",
            titleHi = "विद्युत धारा, प्रतीक और सरल परिपथ",
            instructor = "Sandeep Chawla Sir",
            durationMinutes = 25,
            xpReward = 70,
            diagramType = DiagramType.ELECTRIC_CIRCUIT,
            chapters = listOf(
                LectureChapter("00:00", "Cell, Battery, Switch & Bulb Symbols", "सेल, बैटरी, स्विच और बल्ब के प्रतीक", 0.0f),
                LectureChapter("09:40", "Open vs Closed Electric Circuit", "खुला बनाम बंद विद्युत परिपथ", 0.44f),
                LectureChapter("18:00", "Heating & Magnetic Effect of Current", "विद्युत धारा का तापीय और चुंबकीय प्रभाव", 0.76f)
            ),
            keyFormulas = listOf("Closed Circuit -> Switch ON -> Current Flows (+ to -)", "Heating Effect: H = I²Rt (Fuse wire protection)"),
            transcriptEn = "An electric circuit is a continuous closed path for electric current to flow from the positive terminal of a cell to the negative terminal through a switch and a bulb.",
            transcriptHi = "विद्युत परिपथ (Electric Circuit) वह बंद पथ है जिसमें विद्युत धारा सेल के धन टर्मिनल (+) से ऋण टर्मिनल (-) की ओर प्रवाहित होती है। स्विच ऑन करने पर परिपथ पूरा होता है और बल्ब जल उठता है।",
            defaultSummaryEn = listOf(
                "Current flows only when the circuit path is completely closed (Switch ON).",
                "Conventional current direction is from Positive (+) to Negative (-) terminal.",
                "A fuse is a safety device working on the heating effect of electric current."
            ),
            defaultSummaryHi = listOf(
                "विद्युत धारा तभी बहती है जब परिपथ पूर्णतः बंद (Closed) होता है।",
                "धारा की दिशा सेल के धन (+) सिरे से ऋण (-) सिरे की ओर मानी जाती है।",
                "विद्युत फ्यूज धारा के तापीय प्रभाव पर काम करने वाला सुरक्षा उपकरण है।"
            ),
            diagramLabelsEn = listOf("Battery (+ / -)", "Glowing Bulb Load", "Closed Key/Switch", "Current Flow (I)"),
            diagramLabelsHi = listOf("बैटरी (+ / -)", "जलता हुआ बल्ब", "बंद कुंजी (Switch ON)", "धारा प्रवाह (I)")
        ),
        // Class 8
        LectureItem(
            id = "lec_c8_bio",
            classNumber = 8,
            subjectEn = "Science (Biology)",
            subjectHi = "विज्ञान (जीव विज्ञान)",
            stream = SubjectStream.FOUNDATION,
            titleEn = "Cell Structure: Nucleus, Mitochondria & Organelles",
            titleHi = "कोशिका संरचना: केंद्रक, माइटोकॉन्ड्रिया और कोशिकांग",
            instructor = "Dr. Meenakshi Rao",
            durationMinutes = 26,
            xpReward = 75,
            diagramType = DiagramType.CELL_STRUCTURE,
            chapters = listOf(
                LectureChapter("00:00", "Discovery of Cell by Robert Hooke", "रॉबर्ट हुक द्वारा कोशिका की खोज", 0.0f),
                LectureChapter("09:20", "Cell Membrane, Cytoplasm & Nucleus", "कोशिका झिल्ली, कोशिका द्रव्य और केंद्रक", 0.40f),
                LectureChapter("18:30", "Plant Cell vs Animal Cell Comparison", "पादप कोशिका बनाम जंतु कोशिका में अंतर", 0.75f)
            ),
            keyFormulas = listOf("Mitochondria = Powerhouse of the Cell (ATP)", "Nucleus = Control Center (Chromosomes/DNA)", "Plant Cell has Cell Wall + Chloroplasts"),
            transcriptEn = "The cell is the basic structural and functional unit of life. Inside the cell membrane lies jelly-like cytoplasm, the control-center Nucleus, and the powerhouse Mitochondria!",
            transcriptHi = "कोशिका जीवन की सबसे छोटी संरचनात्मक और कार्यात्मक इकाई है। इसके मुख्य भाग कोशिका झिल्ली, कोशिका द्रव्य और केंद्रक (Nucleus) हैं। माइटोकॉन्ड्रिया को कोशिका का पावरहाउस कहते हैं।",
            defaultSummaryEn = listOf(
                "Robert Hooke discovered cells in 1665 while observing cork slices.",
                "Mitochondria generate energy in the form of ATP (Powerhouse of the cell).",
                "Plant cells have a rigid Cell Wall, Plastids (Chloroplast), and a large central vacuole."
            ),
            defaultSummaryHi = listOf(
                "रॉबर्ट हुक ने 1665 में कॉर्क के टुकड़े में सर्वप्रथम कोशिका की खोज की।",
                "माइटोकॉन्ड्रिया ऊर्जा (ATP) बनाता है, इसलिए इसे कोशिका का ऊर्जा गृह कहते हैं।",
                "पादप कोशिका में कोशिका भित्ति (Cell Wall) और हरित लवक (Chloroplast) पाए जाते हैं जो जंतु कोशिका में नहीं होते।"
            ),
            diagramLabelsEn = listOf("Cell Membrane / Wall", "Nucleus (Control Center)", "Mitochondria (ATP)", "Cytoplasm & Vacuole"),
            diagramLabelsHi = listOf("कोशिका भित्ति / झिल्ली", "केंद्रक (Nucleus)", "माइटोकॉन्ड्रिया (ऊर्जा गृह)", "कोशिका द्रव्य और रिक्तिका")
        ),
        // Class 9
        LectureItem(
            id = "lec_c9_sci",
            classNumber = 9,
            subjectEn = "Physics & Mathematics",
            subjectHi = "भौतिकी और गणित",
            stream = SubjectStream.SCIENCE_MATH,
            titleEn = "Laws of Motion, Coordinate Geometry & Linear Graphs",
            titleHi = "गति के नियम, निर्देशांक ज्यामिति और आलेख",
            instructor = "Aakash Deshmukh Sir (IIT-B)",
            durationMinutes = 30,
            xpReward = 85,
            diagramType = DiagramType.PARABOLA_GRAPH,
            chapters = listOf(
                LectureChapter("00:00", "Velocity-Time Graphs & Slope = Acceleration", "वेग-समय ग्राफ और ढाल = त्वरण", 0.0f),
                LectureChapter("11:00", "Equations of Motion (v=u+at, s=ut+½at²)", "गति के तीनों समीकरणों का ग्राफीय निगमन", 0.42f),
                LectureChapter("21:30", "Newton's Second Law F = ma Numericals", "न्यूटन का द्वितीय नियम (F = ma) और प्रश्न", 0.75f)
            ),
            keyFormulas = listOf("v = u + at", "s = ut + ½at²", "v² - u² = 2as", "F = m · a = Δp / Δt"),
            transcriptEn = "In Class 9 Physics, the slope of a velocity-time graph gives acceleration, and the area under the v-t graph gives displacement! Combined with Newton's Second Law F = ma, we can solve any mechanics problem.",
            transcriptHi = "कक्षा 9 भौतिकी में वेग-समय (v-t) ग्राफ की ढाल (Slope) हमें त्वरण (Acceleration) देती है, और ग्राफ के नीचे का क्षेत्रफल विस्थापन (Displacement) बताता है।",
            defaultSummaryEn = listOf(
                "First Law (Inertia): An object stays at rest or uniform motion unless acted upon by an external force.",
                "Second Law: Rate of change of momentum is directly proportional to applied force (F = ma).",
                "Area under Velocity-Time graph equals total displacement (s)."
            ),
            defaultSummaryHi = listOf(
                "प्रथम नियम (जड़त्व): बाह्य बल के बिना वस्तु अपनी विराम या एकसमान गति की अवस्था बनाए रखती है।",
                "द्वितीय नियम: संवेग परिवर्तन की दर लगाए गए बल के समानुपाती होती है (F = ma)।",
                "वेग-समय ग्राफ के नीचे का क्षेत्रफल कुल विस्थापन (s) के बराबर होता है।"
            ),
            diagramLabelsEn = listOf("Y-Axis (Velocity v)", "X-Axis (Time t)", "Slope = Acceleration (a)", "Area = Displacement (s)"),
            diagramLabelsHi = listOf("Y-अक्ष (वेग v)", "X-अक्ष (समय t)", "ढाल = त्वरण (a)", "क्षेत्रफल = विस्थापन (s)")
        ),
        // Class 10 - Math & Science (Board Exam Special)
        LectureItem(
            id = "lec_c10_math",
            classNumber = 10,
            subjectEn = "Mathematics (Board Special)",
            subjectHi = "गणित (बोर्ड परीक्षा स्पेशल)",
            stream = SubjectStream.SCIENCE_MATH,
            titleEn = "Trigonometry Ratios, Identities & Heights-Distances",
            titleHi = "त्रिकोणमिति अनुपात, सर्वसमिकाएँ और ऊँचाई-दूरी",
            instructor = "Aakash Deshmukh Sir (IIT-B)",
            durationMinutes = 35,
            xpReward = 100,
            diagramType = DiagramType.TRIGONOMETRY_CIRCLE,
            chapters = listOf(
                LectureChapter("00:00", "SOH-CAH-TOA & Pandit Badri Prasad Trick", "लाल/कक्का (LAL/KKA) और त्रिकोणमितीय अनुपात", 0.0f),
                LectureChapter("12:30", "Standard Angles Table (0°, 30°, 45°, 60°, 90°)", "मानक कोणों की सारणी याद करने की ट्रिक", 0.38f),
                LectureChapter("24:00", "Pythagorean Identities: sin²θ + cos²θ = 1", "त्रिकोणमितीय सर्वसमिकाएँ और बोर्ड प्रश्न", 0.72f)
            ),
            keyFormulas = listOf(
                "sin θ = Perpendicular / Hypotenuse (लंब / कर्ण)",
                "cos θ = Base / Hypotenuse (आधार / कर्ण)",
                "sin²θ + cos²θ = 1 | 1 + tan²θ = sec²θ | 1 + cot²θ = cosec²θ"
            ),
            transcriptEn = "Welcome Class 10 students! Trigonometry is the most scoring chapter in Board Exams. Whether you use SOH-CAH-TOA in English or LAL/KKA in Hindi, understanding the right triangle and unit circle makes every identity crystal clear.",
            transcriptHi = "नमस्ते कक्षा 10 के विद्यार्थियों! बोर्ड परीक्षा में त्रिकोणमिति (Trigonometry) सबसे अधिक अंक दिलाने वाला अध्याय है। समकोण त्रिभुज में 'लाल / कक्का' (लंब, आधार, कर्ण) और इकाई वृत्त की मदद से आप हर सूत्र आसानी से समझ सकते हैं।",
            defaultSummaryEn = listOf(
                "In a right triangle: sin θ = P/H, cos θ = B/H, tan θ = P/B = sin θ / cos θ.",
                "Key values: sin 30° = 1/2, sin 45° = 1/√2, sin 60° = √3/2, tan 45° = 1.",
                "Master Identity: sin²θ + cos²θ = 1 (Divide by cos²θ to get 1 + tan²θ = sec²θ)."
            ),
            defaultSummaryHi = listOf(
                "समकोण त्रिभुज में: sin θ = लंब/कर्ण, cos θ = आधार/कर्ण, tan θ = लंब/आधार।",
                "मुख्य मान: sin 30° = 1/2, sin 45° = 1/√2, sin 60° = √3/2, tan 45° = 1।",
                "प्रमुख सर्वसमिका: sin²θ + cos²θ = 1, 1 + tan²θ = sec²θ, तथा 1 + cot²θ = cosec²θ।"
            ),
            diagramLabelsEn = listOf("Hypotenuse (कर्ण H)", "Perpendicular (लंब P)", "Base (आधार B)", "Angle θ (Unit Circle)"),
            diagramLabelsHi = listOf("कर्ण (Hypotenuse)", "लंब (Perpendicular)", "आधार (Base)", "कोण θ (इकाई वृत्त)")
        ),
        LectureItem(
            id = "lec_c10_bio",
            classNumber = 10,
            subjectEn = "Science (Biology)",
            subjectHi = "विज्ञान (जीव विज्ञान)",
            stream = SubjectStream.SCIENCE_MATH,
            titleEn = "Life Processes: Human Heart & Double Circulation",
            titleHi = "जैव प्रक्रम: मानव हृदय और दोहरा परिसंचरण",
            instructor = "Dr. Neha Agnani (AIIMS)",
            durationMinutes = 32,
            xpReward = 95,
            diagramType = DiagramType.HUMAN_HEART,
            chapters = listOf(
                LectureChapter("00:00", "4 Chambers: Left/Right Atrium & Ventricles", "मानव हृदय के 4 कोष्ठ: आलिंद और निलय", 0.0f),
                LectureChapter("11:20", "Pulmonary vs Systemic Circulation Path", "फुफ्फुसीय बनाम दैहिक रक्त परिसंचरण", 0.40f),
                LectureChapter("22:10", "How to Draw Neat Board Exam Heart Diagram", "बोर्ड परीक्षा में मानव हृदय का नामांकित चित्र कैसे बनाएं", 0.74f)
            ),
            keyFormulas = listOf(
                "Pulmonary Vein -> Left Atrium -> Left Ventricle -> Aorta (Oxygenated Blood O2)",
                "Vena Cava -> Right Atrium -> Right Ventricle -> Pulmonary Artery (Deoxygenated CO2)"
            ),
            transcriptEn = "The human heart has four chambers to prevent mixing of oxygenated and deoxygenated blood. Left side handles oxygen-rich blood from lungs to body via the Aorta; Right side pumps CO2-rich blood to the lungs for purification.",
            transcriptHi = "मानव हृदय में चार कोष्ठ (दो आलिंद और दो निलय) होते हैं ताकि ऑक्सीजन युक्त (शुद्ध) और कार्बन डाइऑक्साइड युक्त (अशुद्ध) रक्त आपस में न मिलें। बायां भाग शुद्ध रक्त को महाधमनी (Aorta) द्वारा शरीर में भेजता है।",
            defaultSummaryEn = listOf(
                "4 Chambers: Right Atrium, Right Ventricle (Deoxygenated) & Left Atrium, Left Ventricle (Oxygenated).",
                "Double Circulation: Blood passes through the heart twice in one complete cycle.",
                "Arteries carry blood away from heart (thick walls); Veins bring blood back (have valves)."
            ),
            defaultSummaryHi = listOf(
                "हृदय के 4 कोष्ठ: दायां आलिंद व दायां निलय (अशुद्ध रक्त), बायां आलिंद व बायां निलय (शुद्ध रक्त)।",
                "दोहरा परिसंचरण: एक पूर्ण चक्र में रक्त हृदय से दो बार गुजरता है।",
                "महाधमनी (Aorta) शुद्ध रक्त शरीर में ले जाती है; फुफ्फुसीय धमनी अपवाद है जो अशुद्ध रक्त फेफड़ों तक ले जाती है।"
            ),
            diagramLabelsEn = listOf("Aorta (महाधमनी)", "Pulmonary Artery/Vein", "Left Atrium & Ventricle (O2)", "Right Atrium & Ventricle (CO2)"),
            diagramLabelsHi = listOf("महाधमनी (Aorta)", "फुफ्फुसीय धमनी/शिरा", "बायां आलिंद व निलय (शुद्ध)", "दायां आलिंद व निलय (अशुद्ध)")
        ),
        // Class 11 - Science & Commerce & Humanities
        LectureItem(
            id = "lec_c11_econ",
            classNumber = 11,
            subjectEn = "Economics & Commerce",
            subjectHi = "अर्थशास्त्र एवं वाणिज्य (Commerce)",
            stream = SubjectStream.COMMERCE,
            titleEn = "Microeconomics: Law of Demand, Supply & Market Equilibrium",
            titleHi = "व्यष्टि अर्थशास्त्र: मांग का नियम, पूर्ति और बाजार संतुलन",
            instructor = "CA Rajat Malhotra",
            durationMinutes = 34,
            xpReward = 90,
            diagramType = DiagramType.DEMAND_SUPPLY,
            chapters = listOf(
                LectureChapter("00:00", "Law of Demand & Downward Sloping Curve", "मांग का नियम और नीचे गिरता मांग वक्र (DD)", 0.0f),
                LectureChapter("12:00", "Law of Supply & Upward Sloping Curve", "पूर्ति का नियम और ऊपर उठता पूर्ति वक्र (SS)", 0.40f),
                LectureChapter("23:30", "Market Equilibrium Price & Excess Demand/Supply", "बाजार संतुलन कीमत (Equilibrium Point E)", 0.75f)
            ),
            keyFormulas = listOf("Price Elasticity Ed = (% Change in Quantity) / (% Change in Price)", "At Equilibrium (E): Quantity Demanded (Qd) = Quantity Supplied (Qs)"),
            transcriptEn = "In Microeconomics, Ceteris Paribus (other things remaining constant), when price falls, quantity demanded rises, creating a downward-sloping Demand Curve. Where Demand (DD) intersects Supply (SS), Market Equilibrium is achieved!",
            transcriptHi = "अर्थशास्त्र में अन्य बातें समान रहने पर, जब किसी वस्तु की कीमत घटती है तो उसकी मांग बढ़ती है (मांग का नियम)। जहाँ मांग वक्र (DD) और पूर्ति वक्र (SS) एक-दूसरे को काटते हैं, वहाँ बाजार संतुलन कीमत निर्धारित होती है।",
            defaultSummaryEn = listOf(
                "Law of Demand: Inverse relationship between Price (P) and Quantity Demanded (Qd).",
                "Law of Supply: Direct relationship between Price (P) and Quantity Supplied (Qs).",
                "Equilibrium Point (E): Intersection of Demand and Supply curves where Qd = Qs."
            ),
            defaultSummaryHi = listOf(
                "मांग का नियम: कीमत और मांगी गई मात्रा के बीच विपरीत (ऋणात्मक) संबंध होता है।",
                "पूर्ति का नियम: कीमत और पूर्ति की मात्रा के बीच सीधा (धनात्मक) संबंध होता है।",
                "संतुलन बिंदु (E): जहाँ मांग वक्र (DD) और पूर्ति वक्र (SS) एक-दूसरे को काटते हैं (Qd = Qs)।"
            ),
            diagramLabelsEn = listOf("Price Axis (Y)", "Demand Curve (DD)", "Supply Curve (SS)", "Equilibrium Point (E)"),
            diagramLabelsHi = listOf("कीमत अक्ष (Price Y)", "मांग वक्र (DD)", "पूर्ति वक्र (SS)", "संतुलन बिंदु (E)")
        ),
        // Class 12 - Physics & Calculus & Humanities
        LectureItem(
            id = "lec_c12_phys",
            classNumber = 12,
            subjectEn = "Physics (Class 12 / JEE-NEET)",
            subjectHi = "भौतिकी (कक्षा 12 / JEE-NEET)",
            stream = SubjectStream.SCIENCE_MATH,
            titleEn = "Ray Optics: Convex Lens Formula, Magnification & TIR",
            titleHi = "किरण प्रकाशिकी: उत्तल लेंस सूत्र, आवर्धन और पूर्ण आंतरिक परावर्तन",
            instructor = "Aakash Deshmukh Sir (IIT-B)",
            durationMinutes = 40,
            xpReward = 110,
            diagramType = DiagramType.RAY_OPTICS,
            chapters = listOf(
                LectureChapter("00:00", "Sign Convention & Ray Tracing Rules", "चिह्न परिपाटी और किरण आरेख के नियम", 0.0f),
                LectureChapter("14:00", "Thin Lens Formula (1/v - 1/u = 1/f) & Maker's Formula", "पतले लेंस का सूत्र और लेंस निर्माता सूत्र", 0.38f),
                LectureChapter("28:30", "Total Internal Reflection (TIR) & Optical Fibers", "पूर्ण आंतरिक परावर्तन और प्रकाशिक तंतु", 0.72f)
            ),
            keyFormulas = listOf("Lens Formula: 1/v - 1/u = 1/f", "Magnification m = h'/h = v/u", "Power P = 1 / f(in meters) Dioptre", "Critical Angle: sin i_c = 1 / μ"),
            transcriptEn = "In Class 12 Ray Optics, a parallel ray passing through a convex lens converges at the principal focus F2, while a ray passing through optical center O goes undeviated. Using 1/v - 1/u = 1/f with Cartesian signs solves every board and JEE/NEET problem.",
            transcriptHi = "कक्षा 12 किरण प्रकाशिकी (Ray Optics) में मुख्य अक्ष के समांतर आने वाली प्रकाश किरण उत्तल लेंस से अपवर्तन के बाद द्वितीय फोकस (F2) से होकर गुजरती है, और प्रकाशिक केंद्र (O) से जाने वाली किरण बिना मुड़े सीधी निकल जाती है।",
            defaultSummaryEn = listOf(
                "Convex lens is converging (focal length f is positive); Concave lens is diverging (f is negative).",
                "Lens Maker's Formula: 1/f = (μ - 1)(1/R1 - 1/R2).",
                "Total Internal Reflection occurs when light travels from denser to rarer medium with i > critical angle ic."
            ),
            defaultSummaryHi = listOf(
                "उत्तल लेंस अभिसारी होता है (फोकस दूरी +f); अवतल लेंस अपसारी होता है (फोकस दूरी -f)।",
                "लेंस सूत्र: 1/v - 1/u = 1/f तथा आवर्धन m = v/u।",
                "पूर्ण आंतरिक परावर्तन (TIR) के लिए प्रकाश सघन से विरल माध्यम में जाना चाहिए और आपतन कोण क्रांतिक कोण से बड़ा होना चाहिए।"
            ),
            diagramLabelsEn = listOf("Object AB (beyond F1)", "Convex Lens (Optical Center O)", "Principal Focus F2", "Real Inverted Image A'B'"),
            diagramLabelsHi = listOf("वस्तु AB (F1 से परे)", "उत्तल लेंस (प्रकाशिक केंद्र O)", "मुख्य फोकस F2", "वास्तविक उल्टा प्रतिबिंब A'B'")
        ),
        LectureItem(
            id = "lec_c12_arts",
            classNumber = 12,
            subjectEn = "History & Political Science",
            subjectHi = "इतिहास और राजनीति विज्ञान (Humanities)",
            stream = SubjectStream.HUMANITIES,
            titleEn = "Indian Constitution, Fundamental Rights & Modern History",
            titleHi = "भारतीय संविधान, मौलिक अधिकार और आधुनिक भारत का इतिहास",
            instructor = "Prof. Arvind Mishra (DU)",
            durationMinutes = 36,
            xpReward = 100,
            diagramType = DiagramType.FRACTION_PIE,
            chapters = listOf(
                LectureChapter("00:00", "Constituent Assembly & Preamble Ideals", "संविधान सभा और प्रस्तावना के आदर्श", 0.0f),
                LectureChapter("12:40", "6 Fundamental Rights (Articles 14 to 32)", "6 मौलिक अधिकार (अनुच्छेद 14 से 32)", 0.40f),
                LectureChapter("25:00", "Parliamentary Democracy & Federal Balance", "संसदीय लोकतंत्र और संघीय ढाँचा", 0.74f)
            ),
            keyFormulas = listOf("Article 14-18: Right to Equality", "Article 19-22: Right to Freedom", "Article 32: Heart & Soul of Constitution (Constitutional Remedies)"),
            transcriptEn = "Dr. B.R. Ambedkar called Article 32 the very heart and soul of the Indian Constitution because it empowers citizens to approach the Supreme Court directly for enforcement of Fundamental Rights.",
            transcriptHi = "डॉ. बी.आर. अम्बेडकर ने अनुच्छेद 32 (संवैधानिक उपचारों का अधिकार) को भारतीय संविधान की आत्मा और हृदय कहा है क्योंकि यह नागरिकों के मौलिक अधिकारों की रक्षा की गारंटी देता है।",
            defaultSummaryEn = listOf(
                "Part III (Articles 12–35) of the Indian Constitution guarantees 6 Fundamental Rights.",
                "Preamble declares India a Sovereign, Socialist, Secular, Democratic Republic.",
                "Directive Principles (Part IV) guide the state in establishing social and economic democracy."
            ),
            defaultSummaryHi = listOf(
                "भारतीय संविधान के भाग III (अनुच्छेद 12–35) में नागरिकों को 6 मौलिक अधिकार दिए गए हैं।",
                "प्रस्तावना भारत को संपूर्ण प्रभुत्व-संपन्न, समाजवादी, पंथनिरपेक्ष, लोकतंत्रात्मक गणराज्य घोषित करती है।",
                "अनुच्छेद 32 के तहत सर्वोच्च न्यायालय मौलिक अधिकारों की रक्षा के लिए 5 प्रकार की रिट जारी कर सकता है।"
            ),
            diagramLabelsEn = listOf("Legislature (संसद)", "Executive (कार्यपालिका)", "Judiciary (न्यायपालिका)", "Fundamental Rights (Part III)"),
            diagramLabelsHi = listOf("विधायिका (संसद)", "कार्यपालिका (सरकार)", "न्यायपालिका (सुप्रीम कोर्ट)", "मौलिक अधिकार (भाग 3)")
        )
    )

    val mockTests: List<MockTestItem> = listOf(
        MockTestItem(
            id = "test_foundation_1_5",
            classNumber = 5,
            subjectEn = "Maths & EVS Foundation",
            subjectHi = "गणित और पर्यावरण फाउंडेशन (कक्षा 1-5)",
            stream = SubjectStream.FOUNDATION,
            titleEn = "Primary Genius Mock Test (Classes 1–5)",
            titleHi = "प्राइमरी जीनियस मॉक टेस्ट (कक्षा 1–5)",
            durationMinutes = 10,
            xpReward = 80,
            questions = listOf(
                MockQuestion(
                    id = "q_f1",
                    questionEn = "Which gas do green plants release during photosynthesis?",
                    questionHi = "प्रकाश संश्लेषण के दौरान हरे पौधे कौन-सी गैस छोड़ते हैं?",
                    optionsEn = listOf("Carbon Dioxide (CO2)", "Oxygen (O2)", "Nitrogen (N2)", "Hydrogen"),
                    optionsHi = listOf("कार्बन डाइऑक्साइड (CO2)", "ऑक्सीजन (O2)", "नाइट्रोजन (N2)", "हाइड्रोजन"),
                    correctIndex = 1,
                    explanationEn = "Green plants take in Carbon Dioxide and release life-giving Oxygen (O2) during photosynthesis.",
                    explanationHi = "हरे पौधे प्रकाश संश्लेषण में कार्बन डाइऑक्साइड लेते हैं और ऑक्सीजन (O2) गैस बाहर छोड़ते हैं।"
                ),
                MockQuestion(
                    id = "q_f2",
                    questionEn = "Which fraction is equivalent to 1/2?",
                    questionHi = "इनमें से कौन-सी भिन्न 1/2 के बराबर (तुल्य भिन्न) है?",
                    optionsEn = listOf("1/4", "2/4", "3/4", "2/3"),
                    optionsHi = listOf("1/4", "2/4", "3/4", "2/3"),
                    correctIndex = 1,
                    explanationEn = "Multiplying numerator and denominator of 1/2 by 2 gives 2/4.",
                    explanationHi = "1/2 के अंश और हर में 2 से गुणा करने पर 2/4 प्राप्त होता है।"
                ),
                MockQuestion(
                    id = "q_f3",
                    questionEn = "What is the process of water turning into vapour due to sun's heat called?",
                    questionHi = "सूर्य की गर्मी से पानी के भाप बनने की प्रक्रिया को क्या कहते हैं?",
                    optionsEn = listOf("Condensation", "Evaporation", "Freezing", "Melting"),
                    optionsHi = listOf("संघनन (Condensation)", "वाष्पीकरण (Evaporation)", "जमना", "पिघलना"),
                    correctIndex = 1,
                    explanationEn = "Evaporation is the conversion of liquid water into water vapour due to heat.",
                    explanationHi = "गर्मी के कारण द्रव जल का जलवाष्प (भाप) में बदलना वाष्पीकरण कहलाता है।"
                ),
                MockQuestion(
                    id = "q_f4",
                    questionEn = "If you start at 4 on the number line and jump 5 steps forward, where do you reach?",
                    questionHi = "यदि आप संख्या रेखा पर 4 से शुरू करके 5 कदम आगे कूदते हैं, तो कहाँ पहुँचेंगे?",
                    optionsEn = listOf("8", "9", "10", "7"),
                    optionsHi = listOf("8", "9", "10", "7"),
                    correctIndex = 1,
                    explanationEn = "4 + 5 = 9.",
                    explanationHi = "4 + 5 = 9 होता है।"
                )
            )
        ),
        MockTestItem(
            id = "test_middle_6_8",
            classNumber = 8,
            subjectEn = "Science & Social Studies",
            subjectHi = "विज्ञान और सामाजिक विज्ञान (कक्षा 6-8)",
            stream = SubjectStream.FOUNDATION,
            titleEn = "Middle School Olympiad Mock (Classes 6–8)",
            titleHi = "मिडिल स्कूल ओलंपियाड मॉक टेस्ट (कक्षा 6–8)",
            durationMinutes = 12,
            xpReward = 100,
            questions = listOf(
                MockQuestion(
                    id = "q_m1",
                    questionEn = "Which cell organelle is known as the 'Powerhouse of the Cell'?",
                    questionHi = "किस कोशिकांग को 'कोशिका का बिजलीघर (Powerhouse)' कहा जाता है?",
                    optionsEn = listOf("Ribosome", "Mitochondria", "Golgi Body", "Lysosome"),
                    optionsHi = listOf("राइबोसोम", "माइटोकॉन्ड्रिया", "गॉल्जीकाय", "लाइसोसोम"),
                    correctIndex = 1,
                    explanationEn = "Mitochondria produce cellular energy in the form of ATP molecules.",
                    explanationHi = "माइटोकॉन्ड्रिया में कोशिकीय श्वसन द्वारा ATP के रूप में ऊर्जा बनती है।"
                ),
                MockQuestion(
                    id = "q_m2",
                    questionEn = "Between which two planets does the Asteroid Belt lie in our Solar System?",
                    questionHi = "हमारे सौरमंडल में क्षुद्रग्रह पट्टी (Asteroid Belt) किन दो ग्रहों के बीच स्थित है?",
                    optionsEn = listOf("Earth and Mars", "Mars and Jupiter", "Jupiter and Saturn", "Venus and Earth"),
                    optionsHi = listOf("पृथ्वी और मंगल", "मंगल और बृहस्पति", "बृहस्पति और शनि", "शुक्र और पृथ्वी"),
                    correctIndex = 1,
                    explanationEn = "The main Asteroid Belt is located between the orbits of Mars and Jupiter.",
                    explanationHi = "क्षुद्रग्रह पट्टी मंगल (Mars) और बृहस्पति (Jupiter) की कक्षाओं के बीच पाई जाती है।"
                ),
                MockQuestion(
                    id = "q_m3",
                    questionEn = "Safety fuse wire in electric circuits works on which effect of electric current?",
                    questionHi = "विद्युत परिपथ में सुरक्षा फ्यूज तार विद्युत धारा के किस प्रभाव पर कार्य करता है?",
                    optionsEn = listOf("Magnetic Effect", "Heating Effect", "Chemical Effect", "Optical Effect"),
                    optionsHi = listOf("चुंबकीय प्रभाव", "तापीय प्रभाव (Heating Effect)", "रासायनिक प्रभाव", "प्रकाशीय प्रभाव"),
                    correctIndex = 1,
                    explanationEn = "When excessive current flows, the fuse wire melts due to the heating effect (H = I²Rt) and breaks the circuit.",
                    explanationHi = "अत्यधिक धारा बहने पर तापीय प्रभाव के कारण फ्यूज तार पिघलकर परिपथ को तोड़ देता है।"
                )
            )
        ),
        MockTestItem(
            id = "test_board_9_10",
            classNumber = 10,
            subjectEn = "Maths & Science (Board Exam)",
            subjectHi = "गणित एवं विज्ञान (कक्षा 9-10 बोर्ड)",
            stream = SubjectStream.SCIENCE_MATH,
            titleEn = "Class 10 Board Booster Mock Test",
            titleHi = "कक्षा 10 बोर्ड बूस्टर मॉक टेस्ट",
            durationMinutes = 15,
            xpReward = 120,
            questions = listOf(
                MockQuestion(
                    id = "q_b1",
                    questionEn = "If sin θ = 3/5 in a right-angled triangle, what is the value of cos θ?",
                    questionHi = "यदि किसी समकोण त्रिभुज में sin θ = 3/5 है, तो cos θ का मान क्या होगा?",
                    optionsEn = listOf("4/5", "3/4", "5/4", "5/3"),
                    optionsHi = listOf("4/5", "3/4", "5/4", "5/3"),
                    correctIndex = 0,
                    explanationEn = "By sin²θ + cos²θ = 1 => cos θ = √(1 - 9/25) = √(16/25) = 4/5.",
                    explanationHi = "पाइथागोरस प्रमेय या sin²θ + cos²θ = 1 से: आधार = √(5² - 3²) = 4, अतः cos θ = 4/5।"
                ),
                MockQuestion(
                    id = "q_b2",
                    questionEn = "Which blood vessel brings oxygenated blood from the lungs to the Left Atrium of the human heart?",
                    questionHi = "कौन-सी रक्त वाहिका फेफड़ों से ऑक्सीजन युक्त (शुद्ध) रक्त मानव हृदय के बाएं आलिंद में लाती है?",
                    optionsEn = listOf("Pulmonary Artery", "Pulmonary Vein", "Vena Cava", "Aorta"),
                    optionsHi = listOf("फुफ्फुसीय धमनी", "फुफ्फुसीय शिरा (Pulmonary Vein)", "महाशिरा", "महाधमनी"),
                    correctIndex = 1,
                    explanationEn = "Pulmonary Vein is the only vein that carries oxygenated blood from lungs to the left atrium.",
                    explanationHi = "फुफ्फुसीय शिरा (Pulmonary Vein) फेफड़ों से शुद्ध रक्त को बाएं आलिंद (Left Atrium) में लाती है।"
                ),
                MockQuestion(
                    id = "q_b3",
                    questionEn = "What does the slope of a Velocity-Time (v-t) graph represent?",
                    questionHi = "वेग-समय (v-t) ग्राफ की ढाल (Slope) क्या दर्शाती है?",
                    optionsEn = listOf("Displacement", "Speed", "Acceleration", "Momentum"),
                    optionsHi = listOf("विस्थापन", "चाल", "त्वरण (Acceleration)", "संवेग"),
                    correctIndex = 2,
                    explanationEn = "Slope = Δv / Δt, which is the rate of change of velocity, i.e., Acceleration.",
                    explanationHi = "वेग में परिवर्तन की दर (Δv / Δt) त्वरण (Acceleration) कहलाती है।"
                ),
                MockQuestion(
                    id = "q_b4",
                    questionEn = "What is the value of sec² 45° - tan² 45°?",
                    questionHi = "sec² 45° - tan² 45° का मान क्या होता है?",
                    optionsEn = listOf("0", "1", "2", "√2"),
                    optionsHi = listOf("0", "1", "2", "√2"),
                    correctIndex = 1,
                    explanationEn = "By trigonometric identity, sec²θ - tan²θ = 1 for all valid angles θ.",
                    explanationHi = "त्रिकोणमितीय सर्वसमिका 1 + tan²θ = sec²θ के अनुसार sec²θ - tan²θ सदैव 1 होता है।"
                )
            )
        ),
        MockTestItem(
            id = "test_senior_11_12",
            classNumber = 12,
            subjectEn = "Senior Secondary (Science, Commerce & Arts)",
            subjectHi = "सीनियर सेकेंडरी (विज्ञान, वाणिज्य और कला 11-12)",
            stream = SubjectStream.ALL,
            titleEn = "Class 11–12 All-Stream Mastery Test",
            titleHi = "कक्षा 11–12 सर्व-विषय मास्टरी टेस्ट",
            durationMinutes = 15,
            xpReward = 150,
            questions = listOf(
                MockQuestion(
                    id = "q_s1",
                    questionEn = "A convex lens has a focal length of +25 cm. What is its optical power in Dioptres (D)?",
                    questionHi = "एक उत्तल लेंस की फोकस दूरी +25 सेमी है। डाइऑप्टर (D) में इसकी क्षमता क्या होगी?",
                    optionsEn = listOf("+2.5 D", "+4.0 D", "-4.0 D", "+0.25 D"),
                    optionsHi = listOf("+2.5 D", "+4.0 D", "-4.0 D", "+0.25 D"),
                    correctIndex = 1,
                    explanationEn = "Power P = 100 / f(in cm) = 100 / 25 = +4.0 Dioptres.",
                    explanationHi = "लेंस की क्षमता P = 100 / f(सेमी में) = 100 / 25 = +4.0 D।"
                ),
                MockQuestion(
                    id = "q_s2",
                    questionEn = "In Microeconomics, what happens at the Market Equilibrium price?",
                    questionHi = "व्यष्टि अर्थशास्त्र में बाजार संतुलन कीमत (Market Equilibrium) पर क्या होता है?",
                    optionsEn = listOf(
                        "Quantity Demanded > Quantity Supplied",
                        "Quantity Demanded = Quantity Supplied",
                        "Quantity Demanded < Quantity Supplied",
                        "Price becomes zero"
                    ),
                    optionsHi = listOf(
                        "मांग की मात्रा > पूर्ति की मात्रा",
                        "मांग की मात्रा = पूर्ति की मात्रा (Qd = Qs)",
                        "मांग की मात्रा < पूर्ति की मात्रा",
                        "कीमत शून्य हो जाती है"
                    ),
                    correctIndex = 1,
                    explanationEn = "At market equilibrium, the demand curve intersects the supply curve, so Qd = Qs with no excess demand or supply.",
                    explanationHi = "बाजार संतुलन बिंदु पर मांगी गई मात्रा और पूर्ति की गई मात्रा बराबर होती है (Qd = Qs)।"
                ),
                MockQuestion(
                    id = "q_s3",
                    questionEn = "Which Article of the Indian Constitution was described by Dr. B.R. Ambedkar as its 'Heart and Soul'?",
                    questionHi = "भारतीय संविधान के किस अनुच्छेद को डॉ. बी.आर. अम्बेडकर ने 'संविधान का हृदय और आत्मा' कहा था?",
                    optionsEn = listOf("Article 14", "Article 19", "Article 21", "Article 32"),
                    optionsHi = listOf("अनुच्छेद 14", "अनुच्छेद 19", "अनुच्छेद 21", "अनुच्छेद 32"),
                    correctIndex = 3,
                    explanationEn = "Article 32 (Right to Constitutional Remedies) guarantees enforcement of Fundamental Rights via writs.",
                    explanationHi = "अनुच्छेद 32 (संवैधानिक उपचारों का अधिकार) नागरिकों को मौलिक अधिकारों के प्रवर्तन के लिए सीधे सर्वोच्च न्यायालय जाने का अधिकार देता है।"
                )
            )
        )
    )
}
