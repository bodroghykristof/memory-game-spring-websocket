export let data_handler = {
    _data: {},
    _api_get: function (url, callback) {
        return fetch(url, {
            method: 'GET',
            credentials: 'same-origin'
        })
            .then(response => response.json())
            .then(json_response => callback(json_response));
    },
    _api_get_no_callback: function (url) {
        return fetch(url, {
            method: 'GET',
            credentials: 'same-origin'
        })
            .then(response => response.json());
    },
    _api_get_error_callback_only: function (url, error_callback) {
        return fetch(url, {
            method: 'GET',
            credentials: 'same-origin'
        })
            .then(response => {
            	if (response.status === 404) {
            		error_callback();
            	} else {
            		return response.json();
            	}
            });
    },
    _api_post: function (url, data, callback) {
        fetch(url, {
            method: 'POST',
            credentials: 'same-origin',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
            .then(response => response.json())
            .then(json_response => callback(json_response));
        },
    _api_put: function (url, data, callback, errorCallback) {
        fetch(url, {
            method: 'PUT',
            credentials: 'same-origin',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
            .then(response => response.json())
            .then(json_response => callback(json_response))
            .catch(error => errorCallback(error));
    },
    _api_delete: function (url, data, callback) {
        fetch(url, {
            method: 'DELETE',
            credentials: 'same-origin',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
            .then(response => response.json())
            .then(json_response => callback(json_response))
    }
}