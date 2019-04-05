        function submit(i) {
            var form=document.getElementById("hiddenForm");
            var hiddenEle=document.getElementsByName("devID")[0];
            hiddenEle.value=i;
            form.submit();
        }