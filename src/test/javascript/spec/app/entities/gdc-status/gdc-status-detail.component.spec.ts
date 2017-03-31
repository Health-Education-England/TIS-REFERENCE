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
import {GdcStatusDetailComponent} from "../../../../../../main/webapp/app/entities/gdc-status/gdc-status-detail.component";
import {GdcStatusService} from "../../../../../../main/webapp/app/entities/gdc-status/gdc-status.service";
import {GdcStatus} from "../../../../../../main/webapp/app/entities/gdc-status/gdc-status.model";

describe('Component Tests', () => {

	describe('GdcStatus Management Detail Component', () => {
		let comp: GdcStatusDetailComponent;
		let fixture: ComponentFixture<GdcStatusDetailComponent>;
		let service: GdcStatusService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [GdcStatusDetailComponent],
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
					GdcStatusService
				]
			}).overrideComponent(GdcStatusDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(GdcStatusDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(GdcStatusService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new GdcStatus(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.gdcStatus).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
