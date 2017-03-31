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
import {GmcStatusDetailComponent} from "../../../../../../main/webapp/app/entities/gmc-status/gmc-status-detail.component";
import {GmcStatusService} from "../../../../../../main/webapp/app/entities/gmc-status/gmc-status.service";
import {GmcStatus} from "../../../../../../main/webapp/app/entities/gmc-status/gmc-status.model";

describe('Component Tests', () => {

	describe('GmcStatus Management Detail Component', () => {
		let comp: GmcStatusDetailComponent;
		let fixture: ComponentFixture<GmcStatusDetailComponent>;
		let service: GmcStatusService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [GmcStatusDetailComponent],
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
					GmcStatusService
				]
			}).overrideComponent(GmcStatusDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(GmcStatusDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(GmcStatusService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new GmcStatus(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.gmcStatus).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
