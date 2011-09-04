<%@ Control Language="C#" Inherits="System.Web.Mvc.ViewUserControl<dynamic>" %>

<% string curAction = ViewContext.Controller.ValueProvider.GetValue("action").RawValue as string; %> 
<% var p = ViewData.Model as CS631.Data.Project; %>

<div id="left_menu">
    <ul id="left_list">
        <li class="<%: curAction=="Details" ? "current" : "" %>">
            <%: Html.ActionLink("Show", "Details", new { id = p.ProjID })%>
        </li>
        <li class="<%: curAction=="Edit" ? "current" : "" %>">
            <%: Html.ActionLink("Edit", "Edit", new { id = p.ProjID })%>
        </li>
        <li class="<%: curAction=="Bugs" ? "current" : "" %>">
            <%: Html.ActionLink("Bugs", "Bugs", new { id = p.ProjID })%>
        </li>
        <li class="<%: curAction=="Milestones" ? "current" : "" %>">
            <%: Html.ActionLink("Milestones", "Milestones", new { id = p.ProjID })%>
        </li>        
        <li class="<%: curAction=="Members" ? "current" : "" %>">
            <%: Html.ActionLink("Members", "Members", new { id = p.ProjID })%>
        </li>

    </ul>
</div>