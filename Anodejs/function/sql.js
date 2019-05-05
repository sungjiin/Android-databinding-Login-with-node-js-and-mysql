var mysql = require('mysql');
const express = require('express');
const app = express();

module.exports = function(){
  return{
    init: function(){
      return mysql.createConnection({
        host: 'localhost',
        port: 3306,
        user: 'root',
        password: '181515',
        database: 'test_android'
      })
    },
    create_user: function(conn, useremail, userpw, callback){
      conn.query('insert into user(email, password)values("'+useremail+'", "'+userpw+'")', function(error, result, fields){
        if(!error){
          callback(null, '성공적으로 가입하였습니다.');
        }
        else{
          callback(null, '중복된 아이디가 있습니다.');
        }
      })
    },
    login: function(conn, useremail,userpw, callback){
      conn.query('select * from user where email="'+useremail+'"', function(error, result, fields){
        if(error){
          console.log(error);
        }
        else if(useremail !=""||userpw!=""){
          if(result.length==0){
            callback(null, '존재하는 아이디가 있습니다.');
          }else if(result[0].password != userpw){
            callback(null, '비밀번호가 틀립니다.');
          }else{
            callback(null, '로그인 성공!')
          }
        }
        else{
          callback(null, '아이디와 비밀번호를 입력해 주세요.');
        }
      })
    }
  }
}
