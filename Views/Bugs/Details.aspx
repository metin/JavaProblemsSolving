<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/PM.Master" Inherits="System.Web.Mvc.ViewPage<CS631.Data.Bug>" %>

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
            <h1>Show Bug</h1>
            <p></p>

            <table class="details">
                <tr>
                    <th><%: Html.LabelFor(model => model.ProjID) %></th>
                    <td>
                        <%: Model.ProjID %>
                    </td>
                </tr>
                <tr>
                    <th> <%: Html.LabelFor(model => model.DateReported)%></th>
                    <td>
                        <%: Model.DateReported%>
                    </td>
                </tr>
                <tr>
                    <th><%: Html.LabelFor(model => model.Details)%></th>
                    <td>
                        <%: Model.Details %>
                    </td>
                </tr>

                <tr>
                    <th><label>Asigned To</label></th>
                    <td>
                        <%: Model.Details %>
                    </td>
                </tr>

                <tr>
                    <th><label>Status</label></th>
                    <td>
                        <%: Model.Details %>
                    </td>
                </tr>
                <tr>
                    <th> <%: Html.LabelFor(model => model.DateClosed) %></th>
                    <td>
                        <%: Model.DateClosed %>
                    </td>
                </tr>


            </table>
            <div class="clear"></div> 

        </div>
    </div>
    <div class="clear"></div>

</asp:Content>
