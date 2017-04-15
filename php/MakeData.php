<?php

$conn = mysqli_connect("localhost","junsueg5737","a741236985b","junsueg5737");

if(mysqli_connect_errno())
{
    echo "ConnetError : " . mysqli_connect_error();
}