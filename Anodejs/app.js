const express = require('express');
const app = express();
var mysql = require('mysql');
var conn = require('C:\\Users\\Ka\\Desktop\\Nodejs\\Anodejs\\function\\sql.js')();
var con = conn.init();

app.get('/users',(req, res)=>{
  console.log('who get in here/users');
  res.json(users)
});

app.post('/Login', (req, res)=>{
  console.log('who Login here');
  var inputData;

  req.on('data', (data)=>{
    inputData = JSON.parse(data);
  })

  req.on('end',()=>{
    conn.login(con, inputData.Email, inputData.Password, function(error, data){
      if(error){
        res.write(data);
        res.end();
      }
      else{
        res.write(data);
        res.end();
      }
    })
  })
})

app.post('/post', (req, res)=>{
  console.log('who get in here post / users');
  var inputData;

  req.on('data', (data)=>{
    inputData = JSON.parse(data);//inputdata변수에 JSON으로 받아온 데이터를 저장한다.
  })

  req.on('end', () =>{
    //inputdat
    conn.create_user(con, inputData.Email,inputData.Password, function(error, data){
      if(error){
        res.write(data);
        res.end();
      }
      else{
        res.write(data);
        res.end();
      }
    })
  });

})

app.listen(4000, ()=>{
  console.log('connect');
})
