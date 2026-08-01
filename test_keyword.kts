val alt = "Stunning aerial shot of hot air balloons over Cappadocia's unique landscape at sunrise."
val words = alt.split(" ", ",").map { it.trim() }.filter { it.length > 3 }
println(words.takeLast(2).joinToString(" "))
