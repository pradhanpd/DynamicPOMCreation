<!DOCTYPE html>

<html>
  <head>
    <title name="title">Login</title>
    <style type="text/css">
      .label {text-align: right}
      .error {color: red}
    </style>

  </head>

  <body>
    Need to Create an account? <a href="/signup">Signup</a><p>
    <h2>Login</h2>
    <form method="post">
      <table>
        <tr>
          <td class="label">
            Username
          </td>
          <td>
            <input type="text" id="username" name="username" value="${username}">
          </td>
          <td class="error">
          </td>
        </tr>

        <tr>
          <td class="label">
            Password
          </td>
          <td>
            <input type="password" id="password" name="password" value="">
          </td>
          <td class="error">
	    ${login_error}
            
          </td>
        </tr>

      </table>
      <input type="submit" name="submit">
      <!-- <input type="checkbox" name="conditions" checked> I agree to the terms and conditions -->
    </form>
  </body>

</html>
