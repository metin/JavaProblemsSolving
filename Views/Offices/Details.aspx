<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/Site.Master" Inherits="System.Web.Mvc.ViewPage<CS631.Data.Office>" %>

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
            
            <h1>Showing Office</h1>
            <p>Details of building</p>

            <table class="details">
                <tr>
                    <th><%: Html.LabelFor(model => Model.BuildingID)%></th>
                    <td>
                        <%: Model.BuildingID %>
                    </td>
                </tr>
                <tr>
                    <th> <%: Html.LabelFor(model => Model.DeptID)%></th>
                    <td>
                        <%: Model.DeptID %>
                    </td>   
                </tr>
                <tr>
                    <th><%: Html.LabelFor(model => Model.OfficeNumber) %></th>
                    <td>
                        <%: Model.OfficeNumber %>
                    </td>
                </tr>
                <tr>
                    <th><%: Html.LabelFor(model => Model.Area)%></th>
                    <td>
                        <%: String.Format("${0:F}", Model.Area)%>
                    </td>
                </tr>
                <tr>
                    <th><%: Html.LabelFor(model => Model.RoomType)%></th>
                    <td>
                        <%: Model.RoomType%>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <div class="clear"></div> 


</asp:Content>

