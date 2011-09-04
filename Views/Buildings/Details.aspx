<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/Site.Master" Inherits="System.Web.Mvc.ViewPage<CS631.Data.Building>" %>

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

            <h1>Building Details</h1>
            <p>Details of building</p>

            <table class="details">
                <tr>
                    <th><%: Html.LabelFor(model => model.BuildingName)%></th>
                    <td>
                        <%: Model.BuildingName%>
                    </td>
                </tr>
                <tr>
                    <th> <%: Html.LabelFor(model => model.BuildingCode)%></th>
                    <td>
                        <%: Model.BuildingCode%>
                    </td>   
                </tr>
                <tr>
                    <th><%: Html.LabelFor(model => model.YearAcquired)%></th>
                    <td>
                        <%: Model.YearAcquired%>
                    </td>
                </tr>
                <tr>
                    <th><%: Html.LabelFor(model => model.BuildingCost)%></th>
                    <td>
                        <%: String.Format("${0:F}", Model.BuildingCost)%>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <div class="clear"></div> 



</asp:Content>

