package com.example.bitamirshafiee.chatappcompleted

class Message() {
    var id:String? = null
    var text:String? = null
    var name:String? = null
    var photoUrl: String? = null
    var imageUrl: String? = null

    constructor(text:String?, name:String?, photoUrl:String?, imageUrl:String?):this(){
        this.text = text
        this.name = name
        this.photoUrl = photoUrl
        this.imageUrl = imageUrl
    }
}