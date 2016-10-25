class LoginService {
    constructor(baseUrl) {
        this.baseUrl = baseUrl;
    }

    login(username, password) {
        return fetch(this.baseUrl + "/login", {
            method: "POST",
            headers: new Headers({"Content-Type": "application/json"}),
            body: JSON.stringify({
                username: username,
                password: password
            })
        }).then(function(response){
            return response.json().then(function (json) {
                json.status = response.status;
                return json;
            })
        });
    }

}
