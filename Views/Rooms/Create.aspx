<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/Site.Master" Inherits="System.Web.Mvc.ViewPage<CS631.Data.Room>" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
    Create
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">

    <% Html.RenderPartial("SubMenu"); %>

    <div class="grid_12">
        <div class="section"> 
            <h1>New Room</h1>
            <p>Creat new room</p>
            <br />
            <% using (Html.BeginForm()) { %>
                <%: Html.ValidationSummary(true) %>
                <table class="details">
                    <tr>
                        <th><%: Html.LabelFor(model => model.Code) %></th>
                        <td>
                            <%: Html.EditorFor(model => model.Code) %>
                            <%: Html.ValidationMessageFor(model => model.Code) %>
                        </td>
                    </tr>
                    <tr>
                        <th> <%: Html.LabelFor(model => model.BuildingId)%></th>
                        <td>
                            <%: Html.DropDownListFor(model => model.BuildingId, ViewBag.buildings as SelectList)%>
                            <%: Html.ValidationMessageFor(model => model.BuildingId)%>
                        </td>
                    </tr>

                </table>
                <div class="clear"></div> 
                <div>
                    <input type="submit" value="Create" class="action_button"/>
                </div>
                <div class="clear"></div>
            <% } %>
        </div>
    </div>
    <div class="clear"></div> 


</asp:Content>
