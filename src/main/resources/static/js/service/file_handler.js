export let file_handler = {
    _read_file_input: function (element, callback) {
    	let image = element.files[0];
        const reader = new FileReader();
        reader.addEventListener("load", () => callback(reader.result));
        reader.readAsDataURL(image);
    },
    _read_audio_input: function (audio, callback) {
    	const audioBlob = audio[0];
        const reader = new FileReader();
        reader.addEventListener("load", () => callback(reader.result));
        reader.readAsDataURL(audioBlob);
    }
};