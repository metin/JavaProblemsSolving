<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/Site.Master" Inherits="System.Web.Mvc.ViewPage<CS631.Data.Building>" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
    Offices
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">


    <% Html.RenderPartial("SubMenu"); %>

    <div class="grid_2 nopadding">
        <% Html.RenderPartial("LeftMenu", Model, new ViewDataDictionary(Model)); %>
    </div>

    <div class="grid_10">

        <div class="section" > 
            <h1>List Of Offices in department</h1>
            <p />
            <table width="100%">
                <thead>
                    <tr>
                        <th>Office Number</th>
                        <th>Type</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% foreach (var r in Model.Offices())  { %>
                        <tr>
                            <td><%: r.OfficeNumber %> </td>
                            <td><%: r.RoomType %> </td>
                            <td class="action_buttons"> 
                                <%: Html.ActionLink("Show", "Details", "Offices", new { id = r.OfficeID }, new { @class="jqui_button_show", style="padding: 0px;" })%> 
                                <%: Html.ActionLink("Edit", "Edit", "Offices", new { id = r.OfficeID }, new { @class = "jqui_button_edit" })%> 
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>

    </div>
    <div class="clear"></div> 

</asp:Content>
