import {ComponentFixture, TestBed, async} from "@angular/core/testing";
import {MockBackend} from "@angular/http/testing";
import {Http, BaseRequestOptions} from "@angular/http";
import {OnInit} from "@angular/core";
import {DatePipe} from "@angular/common";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs/Rx";
import {DateUtils, DataUtils, JhiLanguageService} from "ng-jhipster";
import {MockLanguageService} from "../../../helpers/mock-language.service";
import {MockActivatedRoute} from "../../../helpers/mock-route.service";
import {DBCDetailComponent} from "../../../../../../main/webapp/app/entities/dbc/dbc-detail.component";
import {DBCService} from "../../../../../../main/webapp/app/entities/dbc/dbc.service";
import {DBC} from "../../../../../../main/webapp/app/entities/dbc/dbc.model";

describe('Component Tests', () => {

	describe('DBC Management Detail Component', () => {
		let comp: DBCDetailComponent;
		let fixture: ComponentFixture<DBCDetailComponent>;
		let service: DBCService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [DBCDetailComponent],
				providers: [
					MockBackend,
					BaseRequestOptions,
					DateUtils,
					DataUtils,
					DatePipe,
					{
						provide: ActivatedRoute,
						useValue: new MockActivatedRoute({id: 123})
					},
					{
						provide: Http,
						useFactory: (backendInstance: MockBackend, defaultOptions: BaseRequestOptions) => {
							return new Http(backendInstance, defaultOptions);
						},
						deps: [MockBackend, BaseRequestOptions]
					},
					{
						provide: JhiLanguageService,
						useClass: MockLanguageService
					},
					DBCService
				]
			}).overrideComponent(DBCDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(DBCDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(DBCService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new DBC(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.dBC).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
