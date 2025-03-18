<html>
<body>
<h3>Get Employee By Id </h3>
<form action = "getEmployee" method = "get" autocomplete="off">
    <input type="hidden" name="action" value="get">
        Enter employee id : <input type="text" name="id" required><br><br>
    <button type="submit">Get Employee Details</button>
</form>

<br>
<form action = "getEmployee" method = "get" autocomplete="off">
    <input type="hidden" name="action" value="getAllByLimit">
        Enter Limit: <input type="text" name="limit" required><br><br>
    <button type="submit">Get Employees</button>
</form>

<br>
<form action = "getEmployee" method = "get">
    <input type="hidden" name="action" value="getAll">
    <button type="submit">Get All Employees</button>
</form>

</body>
</html>