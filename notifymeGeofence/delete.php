<?php
include "connect.php";

$id1=$_POST["stdid"];



$qr="delete from student_detail where EnNo='$id1'";


$result=mysqli_query($con,$qr);

$data=array();

if($result==1)
{
        //$data=mysqli_fetch_array($result);
	$data["yes"]="1";

}
else
{
	$data["yes"]="0";
}

echo json_encode($data);


?>