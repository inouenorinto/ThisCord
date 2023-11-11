console.log("こんにちは");
let preview = document.querySelector("#preview");
console.log(preview);
document.querySelector('#imageInput').addEventListener('change', function(event) {
	console.log('かわった');
	const file = event.target.files[0];
	if (file) {
		const reader = new FileReader();
		reader.onload = function(e) {
			const img = document.querySelector('#cropper-tgt');
			img.src = e.target.result;
			const cropper = new Cropper(img, {
				aspectRatio: 1 / 1,
				dragMode: 'move',
				autoCropArea: 1,
				cropBoxResizable: false,
				cropBoxMovable: false,
				ready: function() {
					cropper.setCropBoxData({
						width: 300,
						height: 300
					});
				},
			});

			document.querySelector('#cropButton').addEventListener('click', function() {
				const editedImageData = cropper.getCroppedCanvas().toDataURL('image/jpeg');
				document.querySelector('#editedImageField').value = editedImageData;
				preview.src = editedImageData;
			});
		};
	reader.readAsDataURL(file);
	}
});