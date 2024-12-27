package domain.training

import com.project.kotlin_android_app.R

enum class TrainingIcon(val title: String, val description: String, val imageSource: Int){
    Yoga("Йога", "Йога помогает улучшить гибкость, снять стресс и укрепить мышечный корсет", R.drawable.ic_yoga),
    FullBodyStrength("Силовая тренировка всего тела", "Полная силовая тренировка развивает все группы мышц с использованием различных упражнений и оборудования.", R.drawable.ic_full_body_strength),
    Cardio("Кардио тренировки", "Кардио тренировка улучшает работу сердца, дыхательной системы и способствует сжиганию жира.", R.drawable.ic_cardio)
}