<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/Site.Master" Inherits="System.Web.Mvc.ViewPage<CS631.Data.Room>" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
    Details
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">

    <% Html.RenderPartial("SubMenu"); %>

    <div class="grid_2 nopadding">
        <% Html.RenderPartial("LeftMenu", Model, new ViewDataDictionary(Model)); %>
    </div>

    <div class="grid_10">
        <div class="section">

            <h1>Room Details</h1>
            <p>Details of room</p>

            <table class="details">
                <tr>
                    <th><%: Html.LabelFor(model => model.BuildingId) %></th>
                    <td>
                        <%: Model. BuildingId  %>
                    </td>
                </tr>
                <tr>
                    <th> <%: Html.LabelFor(model => model.Code)%></th>
                    <td>
                        <%: Model.Code %>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <div class="clear"></div> 

</asp:Content>
