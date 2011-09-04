<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/Site.Master" Inherits="System.Web.Mvc.ViewPage<CS631.Data.Building>" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
    Rooms
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">


    <% Html.RenderPartial("SubMenu"); %>

    <div class="grid_2 nopadding">
        <% Html.RenderPartial("LeftMenu", Model, new ViewDataDictionary(Model)); %>
    </div>

    <div class="grid_10">

        <div class="section" > 
            <h1>List Of Rooms</h1>
            <p />
            <table width="100%">
                <thead>
                    <tr>
                        <th>Building</th>
                        <th>Code</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% foreach (var r in Model.Rooms())  { %>
                        <tr>
                            <td><%: r.BuildingId %> </td>
                            <td><%: r.Code %> </td>
                            <td class="action_buttons"> 
                                <%: Html.ActionLink("Show", "Details", "Rooms", new { id = r.Id }, new { @class="jqui_button_show", style="padding: 0px;" })%> 
                                <%: Html.ActionLink("Edit", "Edit", "Rooms", new { id = r.Id }, new { @class = "jqui_button_edit" })%> 
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>

    </div>
    <div class="clear"></div> 

</asp:Content>
