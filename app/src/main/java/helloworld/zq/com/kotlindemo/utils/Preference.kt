package helloworld.zq.com.kotlindemo.utils

class Preference<T>(val name:String,private val default:T) {
    companion object {
        private val file_name = "kotlin_demo_file"
    }
}