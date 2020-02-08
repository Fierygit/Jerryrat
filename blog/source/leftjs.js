
if (screen && screen.availWidth > 400) {
    console.log("change width!");
    document.getElementById("changedWidth").style.margin = "0px 0px 0px 15%";
    addTypes = "<div style=\"position:absolute;width: 13%;height: 500px;top: 10%;left: 2%;\">\n" +
        "    <div style=\"list-style: none; padding-left: 15px;margin-bottom: 10px;\">分类</div>" +
        "<ul class=\"list-group\">";
    for (let i = 0; i < types.length; i++) {
        addTypes += "        <li onclick='forIndex(" + i + ")' style=\"padding-left: 20%\" class=\"list-group-item\"><a\n" +
            "                style=\"color: black; text-decoration: none\">";
        addTypes += types[i];
        addTypes += "</a></li>\n"
    }

    addTypes += "    </ul>\n" + "</div>";
    document.getElementById("changedWidth").innerHTML += addTypes;
    document.getElementById("changedWidth").innerHTML += "<div style=\"width: 1px; border: dashed lightgray 0.1px;height: 100%;position: fixed; left: 15%;\"></div>\n";
}
