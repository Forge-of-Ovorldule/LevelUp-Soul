package fireforestsoul.levelupsoul

actual fun export() {
    js(
        """
        const data = {};
        for (let i = 0; i < localStorage.length; i++) {
            const key = localStorage.key(i);
            data[key] = localStorage.getItem(key);
        }
        const json = JSON.stringify(data, null, 2);
        const blob = new Blob([json], { type: 'application/json' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'localStorage_export.json';
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        URL.revokeObjectURL(url);
        """
    )
}

actual fun import(onImported: () -> Unit) {
    js(
        """
        const input = document.createElement('input');
        input.type = 'file';
        input.accept = 'application/json';

        input.onchange = (event) => {
            const file = event.target.files[0];
            const reader = new FileReader();
            reader.onload = () => {
                try {
                    const data = JSON.parse(reader.result);
                    for (const key in data) {
                        if (data.hasOwnProperty(key)) {
                            localStorage.setItem(key, data[key]);
                        }
                    }
                    onImported();
                } catch (e) {
                    alert('Ошибка при импорте: ' + e.message);
                }
            };
            reader.readAsText(file);
        };

        document.body.appendChild(input);
        input.click();
        document.body.removeChild(input);
        """
    )
}