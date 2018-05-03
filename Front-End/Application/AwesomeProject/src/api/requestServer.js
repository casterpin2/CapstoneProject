export const requestServer = async (url, data, method) => {
    try {
        console.log('url: ', url)
        console.log('data: ', JSON.stringify(data))
        console.log('method: ', method)
        let response = await fetch(url, {
            method: method,
            body: data
        })
        let responseJson = await response.json()
        return {
            response: responseJson,
            error: null
        }
    }
    catch (error) {
        return {
            response: null,
            error
        }
    }
}