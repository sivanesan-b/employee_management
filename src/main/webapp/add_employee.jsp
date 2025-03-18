
<html>
<body>
<h1>Hello!</h1>
<h3>Add Employee to the Database</h3>
<form action = "addEmployee", method = "post" autocomplete="off">

    <label for="name">Employee name:</label>
    <input type="text" name="name" required><br><br>

    <label for="phone">Employee Phone No:</label>
    <input type="text" name="phone" required><br>

    <h3>Permanent Address</h3>
    <label for="permanent-street">Street Address:</label>
    <input type="text" id="permanent-street" name="permanent-street" required><br><br>

    <label for="permanent-state">State:</label>
    <input type="text" id="permanent-state" name="permanent-state"required><br><br>

    <h3>Current Address</h3>
    <label for="current-street">Street name:</label>
    <input type="text" id="current-street" name="current-street" required><br><br>

    <label for="current-state">State:</label>
    <input type="text" id="current-state" name="current-state" required><br><br>

    <button type="submit">Add Employee</button>

</form>
</body>
</html>