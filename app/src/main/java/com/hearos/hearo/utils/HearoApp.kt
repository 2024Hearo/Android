package com.hearos.hearo.utils

class HearoApp {
    companion object {
        private var instance: HearoApp? = null

        fun getInstance(): HearoApp {
            if (instance == null) {
                instance = HearoApp()
            }
            return instance!!
        }
    }

    // 데이터 스토어 인스턴스를 저장할 변수
    private var dataStore: DataStore? = null

    // 데이터 스토어 인스턴스 반환 또는 생성
    fun getDataStore(): DataStore {
        if (dataStore == null) {
            dataStore = DataStore() // DataStore 인스턴스 생성 로직
        }
        return dataStore!!
    }
}

class DataStore {
    // 사용자 정보를 저장하는 맵 구조체
    private val userInfoMap = mutableMapOf<String, String>()

    // 사용자 정보를 설정하는 메소드
    fun setUserInfo(userId: String, name: String) {
        // 맵에 사용자 ID와 이름 저장
        userInfoMap[userId] = name
    }

    // 사용자 정보를 가져오는 메소드 (예시로 추가)
    fun getUserInfo(userId: String): String? {
        // 맵에서 사용자 ID로 이름을 조회하여 반환
        return userInfoMap[userId]
    }
}
