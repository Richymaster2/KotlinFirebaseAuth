package com.richard.firebasekotlinauth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Login : AppCompatActivity() {
    private lateinit var displayName: TextView
    private lateinit var status: TextView
    private lateinit var logout: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        displayName = findViewById(R.id.nameTextView) as TextView
        status = findViewById(R.id.statusTextView) as TextView
        logout = findViewById(R.id.singoutButton) as Button

        logout.setOnClickListener(){
            auth.signOut()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        isLogin()

    }

    private fun isLogin(){
        val intent = Intent(this@MainActivity, LoginActivity::class.java)

        auth.currentUser?.uid?.let { loadData(it)  } ?: startActivity(intent)

    }

    private fun loadData(userId: String){
        val dataListener = object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    var user: User = dataSnapshot.getValue(User::class.java)
                    displayName.text = user.displayName
                    status.text = user.status
                }

            }

            override fun onCancelled(p0: DatabaseError?) {
                //
            }
        }
        database.reference.child("user")
            .child(userId).addListenerForSingleValueEvent(dataListener)

    }


}