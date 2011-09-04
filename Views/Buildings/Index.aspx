<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/Site.Master" Inherits="System.Web.Mvc.ViewPage<IEnumerable<CS631.Data.Building>>" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
    Index
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">

    <% Html.RenderPartial("SubMenu"); %>

    <div class="grid_12">

        <div class="section" > 
            <h1>List Of Buildings</h1>
            <p />
            <table width="100%">
                <thead>
                    <tr>
                        <th>Building Code</th>
                        <th>Name</th>
                        <th>Year</th>
                        <th>Cost</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% foreach (var p in Model)  { %>
                        <tr>
                            <td><%: p.BuildingCode %> </td>
                            <td><%: p.BuildingName %> </td>
                            <td><%: p.YearAcquired %> </td>
                            <td> <%: String.Format("${0:F}", p.BuildingCost) %></td>
                            <td class="action_buttons"> 
                                <%: Html.ActionLink("Show", "Details", new { id = p. BuildingID }, new { @class="jqui_button_show", style="padding: 0px;" })%> 
                                <%: Html.ActionLink("Edit", "Edit", new { id = p.BuildingID }, new { @class = "jqui_button_edit" })%> 
                                <%: Html.ActionLink("Delete", "Delete", new { id = p.BuildingID }, new { @class = "jqui_button_delete" })%>
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>

    </div>
    <div class="clear"></div>

</asp:Content>

