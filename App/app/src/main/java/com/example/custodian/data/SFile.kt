package com.example.custodian.data

data class SFile (var name: String,
                  var type: String,
                  var size:  Long,
                  var date: Long,
                  var typeOfIcon: Int,
                  var link: String) : java.io.Serializable