<%@ Control Language="C#" Inherits="System.Web.Mvc.ViewUserControl<dynamic>" %>

<% string curAction = ViewContext.Controller.ValueProvider.GetValue("action").RawValue as string; %> 


    <div id="submenu_container">
        <ul id="submenu">
            <li class="<%: curAction=="Index" ? "current" : "" %>">
                <%: Html.ActionLink("All Buldings", "Index")%>
            </li>
            <li class="<%: curAction=="Create" ? "current" : "" %>">
                <%: Html.ActionLink("New Bulding", "Create")%>
            </li>
        </ul>
    </div>
