<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/Site.Master" Inherits="System.Web.Mvc.ViewPage<CS631.Data.Division>" %>

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
            <h1>Show Division</h1>
            <p>Details of division</p>

            <table class="details">
                <tr>
                    <th> <label>Divison No</label> </th>
                    <td>
                        <%: Model.DivisionNo %>
                    </td>
                </tr>
                <tr>
                    <th> <label>Divison Name</label> </th>
                    <td>
                        <%: Model.DivName %>
                    </td>
                </tr>
                <tr>
                    <th> <label>Division Head</label> </th>
                    <td>
                        <%: Model.DivisionHead %>
                    </td>
                </tr>
            </table>

            <div class="clear"></div> 
        </div>
    </div>
    <div class="clear"></div> 
</asp:Content>

