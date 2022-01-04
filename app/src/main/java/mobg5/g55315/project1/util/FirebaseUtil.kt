package mobg5.g55315.project1.util

import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.BuildConfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class FirebaseUtil {
    companion object {
        /** Use emulators only in debug builds  */
        private val sUseEmulators: Boolean = BuildConfig.DEBUG
        private var FIRESTORE: FirebaseFirestore? = null
        private var AUTH: FirebaseAuth? = null
        private var AUTH_UI: AuthUI? = null
        private var FIRESTORAGE: FirebaseStorage? = null


        fun getFirestore(): FirebaseFirestore? {
            if (FIRESTORE == null) {
                FIRESTORE = FirebaseFirestore.getInstance()
                Log.d("AUTH", FirebaseAuth.getInstance().currentUser?.displayName.toString())

                // Connect to the Cloud Firestore emulator when appropriate. The host '10.0.2.2' is a
                // special IP address to let the Android emulator connect to 'localhost'.
                if (sUseEmulators) {
                    FIRESTORE!!.useEmulator("10.0.2.2", 8080)
                }
            }

            return FIRESTORE
        }

        fun getFirestorage(): FirebaseStorage? {
            if (FIRESTORAGE == null) {
                FIRESTORAGE = FirebaseStorage.getInstance()


                // Connect to the Cloud Firestore emulator when appropriate. The host '10.0.2.2' is a
                // special IP address to let the Android emulator connect to 'localhost'.
                if (sUseEmulators) {
                    FIRESTORAGE!!.useEmulator("10.0.2.2", 8080)
                }
            }
            return FIRESTORAGE
        }

        fun getAuth(): FirebaseAuth? {
            if (AUTH == null) {
                AUTH = FirebaseAuth.getInstance()


                // Connect to the Firebase Auth emulator when appropriate. The host '10.0.2.2' is a
                // special IP address to let the Android emulator connect to 'localhost'.
                if (sUseEmulators) {
                    AUTH!!.useEmulator("10.0.2.2", 9099)
                }
            }
            return AUTH
        }

        fun getAuthUI(): AuthUI? {
            if (AUTH_UI == null) {
                AUTH_UI = AuthUI.getInstance()

                // Connect to the Firebase Auth emulator when appropriate. The host '10.0.2.2' is a
                // special IP address to let the Android emulator connect to 'localhost'.
                if (sUseEmulators) {
                    AUTH_UI!!.useEmulator("10.0.2.2", 9099)
                }
            }
            return AUTH_UI
        }

    }
}