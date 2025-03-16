<html>
<body>
<h1>Update Employee! Enter values in the field to be updated</h1>

<form action = "updateEmployee", method = "post" autocomplete="off">
    <h3>Employee Details</h3>
    <label for="name">Enter employee ID : </label>
    <input type = "text" name = "id" ><br>

    <label for="name">Enter employee name:</label>
    <input type="text" name="name" ><br>

    <label for="phone">Employee Phone No:</label>
    <input type="text" name="phone" ><br>

    <h3>Permanent Address</h3>
    <label for="permanent-street">Street Address:</label>
    <input type="text" id="permanent-street" name="permanent-street" ><br><br>

    <label for="permanent-state">State:</label>
    <input type="text" id="permanent-state" name="permanent-state"><br><br>

    <h3>Current Address</h3>
    <label for="current-street">Street name:</label>
    <input type="text" id="current-street" name="current-street" ><br><br>

    <label for="current-state">State:</label>
    <input type="text" id="current-state" name="current-state" ><br><br
    <input type="hidden" name="action" value="update">
    <input type="submit">
</form>
</body>
</html>